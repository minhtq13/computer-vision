package models

type KafkaMessage struct {
	Key        string `json:"key" binding:"required"`
	Topic      string `json:"topic" binding:"required"`
	Partition  int32  `json:"-"`
	MessageId  string `json:"messageId" binding:"required"`
	StatusCode int    `json:"statusCode" binding:"required"`
	Data       any    `json:"data"`
}
