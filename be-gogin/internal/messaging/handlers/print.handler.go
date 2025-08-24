package handlers

import (
	"be-gogin/internal/loggers"
	"be-gogin/internal/models"
	"context"
	"encoding/json"
	"fmt"
)

const LogKafkaMessageHandleLogTopic = "Log-Consumed-Kafka-Message"

type LogKafkaMessageHandler struct{}

func NewLogKafkaMessageHandler() *LogKafkaMessageHandler {
	return &LogKafkaMessageHandler{}
}

func (p *LogKafkaMessageHandler) Handle(_ context.Context, topic string, partition int, offset string, value []byte) error {
	var messageValue models.KafkaMessage
	convErr := json.Unmarshal(value, &messageValue)
	if convErr != nil {
		loggers.Error(LogKafkaMessageHandleLogTopic, convErr)
		return convErr
	}
	// handler raw message value
	patternLog := "Consumed message : %s from topic [%s] in partition [%d] with offset [%s]"
	loggers.Info(LogKafkaMessageHandleLogTopic, fmt.Sprintf(patternLog, string(value), topic, partition, offset))
	return nil
}
