package initializers

import (
	"be-gogin/global"
	"be-gogin/pkg/loggers"

	"go.uber.org/zap"
)

func NewLogger() *zap.Logger {
	var logger *zap.Logger
	logger = loggers.NewLogger(global.AppConfig.Logging)
	global.Logger = logger
	return logger
}
