package confluent

import (
	"be-gogin/internal/errors"
	"be-gogin/internal/loggers"
	"be-gogin/internal/models"
	"be-gogin/pkg/configs"
	"be-gogin/pkg/utils"
	"context"
	"encoding/json"
	"fmt"
	"strings"
	"sync"

	"github.com/confluentinc/confluent-kafka-go/v2/kafka"
)

const DefaultProducerKey = "default"
const KafkaProducerLogTopic = "Confluent-Kafka-Producer"

type KafkaConfluentProducerManager struct {
	producerPool map[string]*kafka.Producer
	poolMutex    sync.RWMutex
}

func NewKafkaConfluentProducerManager(appConfig *configs.AppEnvConfig) (*KafkaConfluentProducerManager, error) {
	// default producer cho app
	kafkaConfig := appConfig.Kafka
	configMap := &kafka.ConfigMap{
		"bootstrap.servers":        strings.Join(kafkaConfig.Brokers, ","),
		"client.id":                kafkaConfig.Producer.ClientID,
		"acks":                     1,
		"retries":                  kafkaConfig.Producer.Retries,
		"allow.auto.create.topics": true,
	}

	// Init Default Kafka Producer
	defaultProducer, initErr := kafka.NewProducer(configMap)
	if initErr != nil {
		errors.LogError(initErr)
		return nil, initErr
	}
	loggers.Info(KafkaProducerLogTopic, "Init default kafka producer successfully")
	producerPool := make(map[string]*kafka.Producer)
	producerPool[DefaultProducerKey] = defaultProducer
	return &KafkaConfluentProducerManager{
		producerPool: producerPool,
	}, nil
}

// ProduceMessage Hàm produce message kafka dạng sync
func (producerManager *KafkaConfluentProducerManager) ProduceMessage(ctx context.Context, producerKey string, topic string, key string, messageData models.KafkaMessage) error {
	defer utils.LogFunction()()

	// Lấy producer từ pool
	producer := producerManager.getProducer(utils.IIf(producerKey != "", producerKey, DefaultProducerKey))
	if producer == nil {
		loggers.Error(KafkaProducerLogTopic, nil, fmt.Sprintf("Not found producer in pool with key [%s]", producerKey))
		return fmt.Errorf("not found producer in pool with key [%s]", producerKey)
	}

	// tạo message kafka
	valueBytes, msErr := json.Marshal(messageData)
	if msErr != nil {
		errors.LogError(msErr)
		return msErr
	}
	kafkaMessage := &kafka.Message{
		TopicPartition: kafka.TopicPartition{Topic: &topic, Partition: kafka.PartitionAny},
		Key:            []byte(key),
		Value:          valueBytes,
	}
	// Tạo delivery channel để nhận kết quả cho message này
	deliveryChan := make(chan kafka.Event, 1)

	// Gửi message và chỉ định delivery channel - SYNC
	produceErr := producer.Produce(kafkaMessage, deliveryChan)
	if produceErr != nil {
		errors.LogError(produceErr)
		return produceErr
	}

	// Chờ delivery report từ channel
	select {
	case e := <-deliveryChan:
		switch ev := e.(type) {
		case *kafka.Message:
			if ev.TopicPartition.Error != nil {
				errors.LogError(ev.TopicPartition.Error)
				return ev.TopicPartition.Error
			}
			loggers.Info(KafkaProducerLogTopic, fmt.Sprintf("Message delivered successfully to topic %s in partition %d, offset %d\n",
				*(ev.TopicPartition.Topic), ev.TopicPartition.Partition, ev.TopicPartition.Offset))
			return nil
		case kafka.Error:
			errors.LogError(ev)
			return ev
		}
	case <-ctx.Done():
		errors.LogError(ctx.Err())
		return ctx.Err()
	}
	return nil
}

// ProduceMessageAsync Hàm produce message async
func (producerManager *KafkaConfluentProducerManager) ProduceMessageAsync(_ context.Context, producerKey string, topic string, key string, messageData models.KafkaMessage) error {
	defer utils.LogFunction()()

	// produce message async
	producer := producerManager.getProducer(producerKey)
	if producer == nil {
		loggers.Error(KafkaProducerLogTopic, nil, fmt.Sprintf("Not found producer in pool with key [%s]", producerKey))
		return fmt.Errorf("not found producer in pool with key [%s]", producerKey)
	}

	// tạo message kafka
	valueBytes, msErr := json.Marshal(messageData)
	if msErr != nil {
		errors.LogError(msErr)
		return msErr
	}
	kafkaMessage := &kafka.Message{
		TopicPartition: kafka.TopicPartition{Topic: &topic, Partition: kafka.PartitionAny},
		Key:            []byte(key),
		Value:          valueBytes,
	}

	// produce message async when specify the delivery channel is nil
	err := producer.Produce(kafkaMessage, nil)
	if err != nil {
		errors.LogError(err)
		return err
	}
	return nil
}

func (producerManager *KafkaConfluentProducerManager) getProducer(key string) *kafka.Producer {
	return producerManager.producerPool[key]
}

func (producerManager *KafkaConfluentProducerManager) Close() {
	utils.LogFunction()()
	producerManager.poolMutex.Lock()
	defer producerManager.poolMutex.Unlock()
	for key, producer := range producerManager.producerPool {
		// flush tất cả các message đang chuẩn bị đc delivery trong 1s tiếp theo
		producer.Flush(1000)
		producer.Close()
		loggers.Info(KafkaProducerLogTopic, fmt.Sprintf("Closed producer with key %s successfully!", key))
	}
}
