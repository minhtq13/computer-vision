package confluent

import (
	"be-gogin/global"
	"be-gogin/global/constants"
	"be-gogin/internal/errors"
	"be-gogin/internal/loggers"
	"be-gogin/internal/messaging"
	"be-gogin/pkg/configs"
	"context"
	"fmt"
	"strconv"
	"strings"

	"github.com/confluentinc/confluent-kafka-go/v2/kafka"
	"github.com/samber/lo"
)

const KafkaConsumerLogTopic = "Confluent-Kafka-Consumer"

type KafkaConfluentConsumerManager struct {
	consumer *kafka.Consumer
}

func NewKafkaConfluentConsumerManager(appConfig *configs.AppEnvConfig) *KafkaConfluentConsumerManager {
	kafkaConfig := appConfig.Kafka
	configMap := &kafka.ConfigMap{
		"bootstrap.servers":               strings.Join(kafkaConfig.Brokers, ","),
		"client.id":                       kafkaConfig.Producer.ClientID,
		"group.id":                        kafkaConfig.Consumer.GroupId,
		"group.instance.id":               kafkaConfig.Consumer.GroupInstanceId,
		"auto.offset.reset":               "earliest",
		"enable.auto.commit":              true,
		"fetch.min.bytes":                 kafkaConfig.Consumer.MinBytes,
		"fetch.max.bytes":                 kafkaConfig.Consumer.MaxBytes,
		"fetch.wait.max.ms":               kafkaConfig.Consumer.MaxWaitInSecond * 1000,
		"go.application.rebalance.enable": true,
	}
	// Khởi tạo consumer
	consumer, initErr := kafka.NewConsumer(configMap)
	if initErr != nil {
		errors.LogError(initErr)
		return nil
	}
	loggers.Info(KafkaConsumerLogTopic, fmt.Sprintf("Initialized Confluent Kafka Consumer with client-id [%s] successfully!", kafkaConfig.Producer.ClientID))
	return &KafkaConfluentConsumerManager{
		consumer: consumer,
	}
}

func (kafkaConsumerManager *KafkaConfluentConsumerManager) Subscribe(context context.Context, topic string, handler messaging.IMessageHandler) {
	subscribeErr := kafkaConsumerManager.consumer.Subscribe(topic, rebalanceCallback)
	if subscribeErr != nil {
		loggers.Error(KafkaConsumerLogTopic, subscribeErr, fmt.Sprintf("Error while subscribing to topic %s", topic))
	}

	// thực hiện subscribe và consume messages từ topic
	go func(handler messaging.IMessageHandler) {
		// recover panic
		defer func() {
			if r := recover(); r != nil {
				loggers.Info(KafkaConsumerLogTopic, "Kafka consumer panic recovered successfully!")
			} else {
				loggers.Error(KafkaConsumerLogTopic, nil, "Kafka consumer panic recovered fail!")
			}
		}()
		for {
			select {
			case <-context.Done():
				loggers.Info(KafkaConsumerLogTopic, fmt.Sprintf("Ended listening and consuming messages from topic %s!", topic))
				return
			default:
				kafkaMessage, readErr := kafkaConsumerManager.consumer.ReadMessage(-1) // -1: infinite wait to consume messages
				if readErr != nil {
					loggers.Error(KafkaConsumerLogTopic, readErr)
				}
				// nếu message đến từ topic đã subscribe thì mới gọi handler
				if *kafkaMessage.TopicPartition.Topic == topic {
					// Handle kafka message with a handler
					handleErr := handler.Handle(context, topic, int(kafkaMessage.TopicPartition.Partition), kafkaMessage.TopicPartition.Offset.String(), kafkaMessage.Value)
					if handleErr != nil {
						loggers.Error(KafkaConsumerLogTopic, handleErr, fmt.Sprintf("Handle message in topic [%v] error", kafkaMessage.TopicPartition.Topic))
					}
				}
			}
		}
	}(handler)
}

// Hàm rebalance call back khi kafka assign/revoke partition từ consumer
func rebalanceCallback(consumer *kafka.Consumer, event kafka.Event) error {
	switch ev := event.(type) {
	case kafka.AssignedPartitions:
		loggers.Info(KafkaConsumerLogTopic, "Kafka rebalance callback called!")
		partitionNames := lo.Map(ev.Partitions, func(item kafka.TopicPartition, index int) string {
			return *item.Topic + constants.DashChar + strconv.FormatInt(int64(item.Partition), 10)
		})
		loggers.Info(KafkaConsumerLogTopic, fmt.Sprintf("Assigned Partitions: [%v]", strings.Join(partitionNames, ",")))
		return consumer.Assign(ev.Partitions)
	case kafka.RevokedPartitions:
		global.Logger.Info("Kafka rebalance callback called!")
		partitionNames := lo.Map(ev.Partitions, func(item kafka.TopicPartition, index int) string {
			return *item.Topic + constants.DashChar + strconv.FormatInt(int64(item.Partition), 10)
		})
		loggers.Info(KafkaConsumerLogTopic, fmt.Sprintf("Revoked Partitions: [%v]", strings.Join(partitionNames, ",")))
		// Commit các offsets trước khi partition bị thu hồi
		_, commitErr := consumer.Commit()
		if commitErr != nil {
			errors.LogError(commitErr)
			return commitErr
		}
	default:
		loggers.Error(KafkaConsumerLogTopic, nil, fmt.Sprintf("Unexpected event %v ", event))
	}
	return nil
}
