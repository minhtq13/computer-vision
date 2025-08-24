package tracers

import (
	"context"
	"time"

	"go.uber.org/zap"
	gormlogger "gorm.io/gorm/logger"
)

type GormLogger struct {
	logger   *zap.Logger
	logLevel gormlogger.LogLevel
}

func NewGormLogger(logger *zap.Logger) gormlogger.Interface {
	return &GormLogger{logger: logger, logLevel: gormlogger.Info}
}

func (gormLogger *GormLogger) LogMode(level gormlogger.LogLevel) gormlogger.Interface {
	return gormLogger
}

func (gormLogger *GormLogger) Info(ctx context.Context, s string, data ...interface{}) {
	if gormLogger.logLevel >= gormlogger.Info {
		gormLogger.logger.Sugar().Infof(s, data...)
	}
}

func (gormLogger *GormLogger) Warn(ctx context.Context, s string, data ...interface{}) {
	if gormLogger.logLevel >= gormlogger.Warn {
		gormLogger.logger.Sugar().Warnf(s, data...)
	}
}

func (gormLogger *GormLogger) Error(ctx context.Context, s string, data ...interface{}) {
	if gormLogger.logLevel >= gormlogger.Error {
		gormLogger.logger.Sugar().Errorf(s, data...)
	}
}

// Trace Hàm trace query trước và sau khi query được thực hiện/**
func (gormLogger *GormLogger) Trace(ctx context.Context, begin time.Time, fc func() (sql string, rowsAffected int64), err error) {
	if gormLogger.logLevel <= 0 {
		return
	}
	// execute query thông qua func truyền vào
	sql, rows := fc()
	elapsed := time.Since(begin) / time.Millisecond
	if err != nil && gormLogger.logLevel >= gormlogger.Error {
		gormLogger.logger.Error("Trace error: ", zap.String("Sql", sql), zap.Error(err), zap.Duration("ExecutionTime", elapsed))
	} else if gormLogger.logLevel >= gormlogger.Info {
		gormLogger.logger.Info("[PostgresGorm]",
			zap.String("Sql", sql),
			zap.Int64("Rows", rows),
			zap.Duration("ExecutionTime", elapsed),
		)
	}
}
