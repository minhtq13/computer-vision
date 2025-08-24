package segmenio

import (
	"be-gogin/global"
	"be-gogin/internal/errors"
	"be-gogin/internal/models"
	"be-gogin/pkg/configs"
	"be-gogin/pkg/utils"
	"context"
	"encoding/json"
	"sync"

	"github.com/segmentio/kafka-go"
)

type KafkaSegIOProducer struct {
	brokers    []string
	writerPool map[string]*kafka.Writer
	poolMutex  sync.RWMutex
}

func NewKafkaSegIOProducer(config *configs.AppEnvConfig) *KafkaSegIOProducer {
	// tạo ra writer với cấu hình
	kafkaConfig := config.Kafka
	return &KafkaSegIOProducer{
		brokers:    kafkaConfig.Brokers,
		writerPool: make(map[string]*kafka.Writer),
	}
}

func (p *KafkaSegIOProducer) getWriter(topic string) *kafka.Writer {
	kafkaConfig := global.AppConfig.Kafka
	// kiểm tra trong pool writer có writer tương ứng của topic hay chưa (lock việc read)
	p.poolMutex.RLock()
	writer, exists := p.writerPool[topic]
	p.poolMutex.RUnlock()

	if exists {
		return writer
	}

	// tạo writer mới và push nó vào writer pool (lock toàn bộ việc write)
	p.poolMutex.Lock()
	defer p.poolMutex.Unlock()
	// kiểm tra lần nữa writer đã có trong pool hay chưa với write lock
	if writer, exists := p.writerPool[topic]; exists {
		return writer
	}
	writer = &kafka.Writer{
		Addr:                   kafka.TCP(kafkaConfig.Brokers...),
		Balancer:               &kafka.LeastBytes{},
		AllowAutoTopicCreation: true,
		BatchSize:              kafkaConfig.Producer.BatchSize,
		MaxAttempts:            kafkaConfig.Producer.Retries,
		RequiredAcks:           kafka.RequireOne, // produce và nhận ack từ lead broker
	}
	// push writer vào pool
	p.writerPool[topic] = writer
	global.Logger.Sugar().Infof("Initialized the new writer for topic %s successfully!", topic)
	return writer
}

func (p *KafkaSegIOProducer) ProduceMessage(ctx context.Context, topic string, key string, messageData models.KafkaMessage) error {
	defer utils.LogFunction()()

	// get writer dựa theo topic
	writer := p.getWriter(topic)

	// tạo message kafka
	value, msErr := json.Marshal(messageData)
	if msErr != nil {
		errors.LogError(msErr)
		return msErr
	}
	kafkaMessage := kafka.Message{
		Topic: messageData.Topic,
		Key:   []byte(key),
		Value: value,
	}

	// produce and push the target message to kafka
	produceErr := writer.WriteMessages(ctx, kafkaMessage)
	if produceErr != nil {
		errors.LogError(produceErr)
		return produceErr
	}
	return nil
}

func (p *KafkaSegIOProducer) ProduceBatchMessages(ctx context.Context, topic string, messageDataList []models.KafkaMessage) error {
	//TODO: implement me
	return nil
}

func (p *KafkaSegIOProducer) Close() error {
	// lock trong khi close toàn bộ writer
	p.poolMutex.Lock()
	defer p.poolMutex.Unlock()
	var closeErrs []error
	for topic, writer := range p.writerPool {
		if err := writer.Close(); err != nil {
			closeErrs = append(closeErrs, err)
			global.Logger.Sugar().Errorf("Failed to close the writer of topic %s!", topic)
			errors.LogError(err)
		}
	}
	if len(closeErrs) > 0 {
		global.Logger.Sugar().Errorf("Failed to close all writers! caused by: %v", closeErrs)
	}
	return nil
}
