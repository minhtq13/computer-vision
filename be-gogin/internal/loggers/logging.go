package loggers

import (
	"be-gogin/global"
	"strings"

	"go.uber.org/zap"
)

func Info(topic string, message ...string) {
	global.Logger.Sugar().Infof("[%s] %s", topic, strings.Join(message, ", "))
}

func Error(topic string, err error, message ...string) {
	global.Logger.Sugar().Errorf("[%s] %s: %v", topic, strings.Join(message, ", "), zap.Error(err))
}

func Panic(topic string, err error, message ...string) {
	global.Logger.Sugar().Panicf("[%s] %s: %v", topic, strings.Join(message, ", "), zap.Error(err))
}
