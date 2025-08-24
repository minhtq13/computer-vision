package controllers

import (
	"be-gogin/global/constants"
	"be-gogin/internal/errors"
	"be-gogin/internal/messaging/confluent"
	"be-gogin/internal/messaging/segmenio"
	"be-gogin/internal/models"
	"be-gogin/pkg/utils"
	"net/http"
	"strconv"
	"time"

	"github.com/gin-gonic/gin"
)

type MessagingController struct {
	kafkaSegIOProducer            *segmenio.KafkaSegIOProducer
	kafkaConfluentProducerManager *confluent.KafkaConfluentProducerManager
}

func NewMessagingController(kafkaSegIOProducer *segmenio.KafkaSegIOProducer, kafkaConfluentProducerManager *confluent.KafkaConfluentProducerManager) *MessagingController {
	return &MessagingController{
		kafkaSegIOProducer:            kafkaSegIOProducer,
		kafkaConfluentProducerManager: kafkaConfluentProducerManager,
	}
}

// ProduceKafkaMessage Hàm gửi sample kafka message
func (messagingController *MessagingController) ProduceKafkaMessage(context *gin.Context) {
	defer utils.LogFunction()()
	var message any
	bindErr := context.ShouldBindBodyWithJSON(&message)
	if bindErr != nil {
		errors.HandleError(context, bindErr)
		return
	}
	// produce the target message to kafka
	kafkaMessage := models.KafkaMessage{
		Topic:     constants.KafkaDefaultTopic,
		Key:       "example",
		MessageId: constants.KafkaDefaultTopic + "_MSG_" + strconv.FormatInt(time.Now().UnixMilli(), 10),
		Data:      message,
	}
	produceErr := messagingController.kafkaConfluentProducerManager.ProduceMessage(context, confluent.DefaultProducerKey, constants.KafkaDefaultTopic, "example", kafkaMessage)
	if produceErr != nil {
		kafkaMessage.StatusCode = constants.ProduceMessageFailCode
		errors.HandleError(context, produceErr)
		return
	}
	// return
	kafkaMessage.StatusCode = constants.ProduceMessageSuccessCode
	context.JSON(http.StatusOK, kafkaMessage)
}
