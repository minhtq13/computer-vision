package segmenio

import (
	"be-gogin/global"
	"be-gogin/internal/errors"
	"be-gogin/internal/messaging"
	"be-gogin/pkg/configs"
	"context"
	"fmt"
	"strconv"
	"sync"
	"time"

	"github.com/segmentio/kafka-go"
)

type KafkaSegIOConsumer struct {
	brokers    []string
	readerPool map[string]*kafka.Reader
	poolMutex  sync.RWMutex
}

func NewKafkaSegIOConsumer(config *configs.AppEnvConfig) *KafkaSegIOConsumer {
	return &KafkaSegIOConsumer{
		brokers:    config.Kafka.Brokers,
		readerPool: make(map[string]*kafka.Reader),
	}
}

func (c *KafkaSegIOConsumer) getReader(topic string, groupId string) *kafka.Reader {
	poolKey := fmt.Sprintf("%s-%s", topic, groupId)
	kafkaConfig := global.AppConfig.Kafka

	// Kiểm tra reader có trong pool hay chưa (acquire read lock)
	c.poolMutex.RLock()
	reader, exists := c.readerPool[poolKey]
	c.poolMutex.RUnlock()
	if exists {
		return reader
	}

	// Nếu chưa có -> push new reader vào pool (acquire write lock)
	c.poolMutex.Lock()
	defer c.poolMutex.Unlock()
	// kiểm tra reader tồn tại thêm 1 lần nữa -> đảm bảo
	if reader, exists := c.readerPool[poolKey]; exists {
		return reader
	}
	reader = kafka.NewReader(kafka.ReaderConfig{
		Brokers:  kafkaConfig.Brokers,
		Topic:    topic,
		GroupID:  groupId,
		MinBytes: kafkaConfig.Consumer.MinBytes, // 1KB
		MaxBytes: kafkaConfig.Consumer.MaxBytes, // 10MB
		MaxWait:  time.Duration(kafkaConfig.Consumer.MaxWaitInSecond) * time.Second,
	})
	// push reader vào pool
	c.readerPool[poolKey] = reader
	global.Logger.Sugar().Infof("Initialized the new reader for topic %s successfully!", topic)
	return reader
}

func (c *KafkaSegIOConsumer) StartConsuming(ctx context.Context, topic, groupId string, handler messaging.IMessageHandler) {
	reader := c.getReader(topic, groupId)
	global.Logger.Sugar().Infof("Starting listen from topic %s with groupID %s ...", topic, groupId)
	// Start 1 goroutine để listen và pull các message từ kafka với topic và groupId
	go func() {
		// recover panic
		defer func() {
			if r := recover(); r != nil {
				global.Logger.Info("======= Kafka consumer panic recovered successfully! =======")
			} else {
				global.Logger.Error("======= Kafka consumer panic recovered fail! =======")
			}
		}()
		for {
			select {
			case <-ctx.Done(): // context cancelled/done
				global.Logger.Sugar().Infof("Ended listen from topic %s with groupID %s ...", topic, groupId)
				return
			default:
				kafkaMessage, err := reader.ReadMessage(ctx)
				if err != nil {
					errors.LogError(err)
					if ctx.Err() != nil {
						return // context cancelled
					}
				}
				// Handle kafka message with a handler
				handleErr := handler.Handle(ctx, topic, kafkaMessage.Partition, strconv.FormatInt(kafkaMessage.Offset, 10), kafkaMessage.Value)
				if handleErr != nil {
					global.Logger.Sugar().Errorf("Handle message in topic [%v] error: %v", kafkaMessage.Topic, handleErr)
					errors.LogError(handleErr)
				}
			}
		}
	}()
}

func (c *KafkaSegIOConsumer) Close() error {
	// lock trong khi close toàn bộ reader
	c.poolMutex.Lock()
	defer c.poolMutex.Unlock()
	var closeErrs []error
	for poolKey, writer := range c.readerPool {
		if err := writer.Close(); err != nil {
			closeErrs = append(closeErrs, err)
			global.Logger.Sugar().Errorf("Failed to close the reader of topic-groupId [%s]!", poolKey)
			errors.LogError(err)
		}
	}
	if len(closeErrs) > 0 {
		global.Logger.Sugar().Errorf("Failed to close all readers! caused by: %v", closeErrs)
	}
	return nil
}
