package loggers

import (
	"be-gogin/pkg/configs"
	"fmt"
	"log"
	"os"
	"time"

	"github.com/natefinch/lumberjack"
	"go.uber.org/zap"
	"go.uber.org/zap/zapcore"
)

func NewLogger(config configs.LoggingConfig) *zap.Logger {
	level := zap.InfoLevel
	switch config.Level {
	case "debug":
		level = zap.DebugLevel
	case "info":
		level = zap.InfoLevel
	case "warn":
		level = zap.WarnLevel
	case "error":
		level = zap.ErrorLevel
	case "panic":
		level = zap.PanicLevel
	case "fatal":
		level = zap.FatalLevel
	default:
	}
	encoder := GetLogEncoder()
	hook := lumberjack.Logger{
		Filename:   config.FileName,
		MaxSize:    config.MaxSizeMBs, // MB
		MaxBackups: config.MaxBackups,
		MaxAge:     config.MaxAgeDays, //days
		Compress:   config.Compress,   // disabled by default
	}
	core := zapcore.NewCore(encoder, zapcore.NewMultiWriteSyncer(zapcore.AddSync(os.Stdout), zapcore.AddSync(&hook)), level)
	return zap.New(core, zap.AddCaller(), zap.AddStacktrace(zap.ErrorLevel))
}

// GetLogEncoder format log encode
func GetLogEncoder() zapcore.Encoder {
	encoderConfig := zap.NewProductionEncoderConfig()
	// long -> time format
	encoderConfig.EncodeTime = zapcore.ISO8601TimeEncoder
	// ts -> Time
	encoderConfig.TimeKey = "timestamp"
	// from INFO
	encoderConfig.EncodeLevel = zapcore.CapitalLevelEncoder
	// caller
	encoderConfig.EncodeCaller = zapcore.ShortCallerEncoder //

	return zapcore.NewConsoleEncoder(encoderConfig)
}

// GetWriterAsync ghi ra file log
func GetWriterAsync() zapcore.WriteSyncer {
	// tạo thư mục logs nếu chưa tồn tại
	logFolder := "logs"
	if _, err := os.Stat(logFolder); os.IsNotExist(err) {
		createDirErr := os.Mkdir(logFolder, 0755)
		if createDirErr != nil {
			log.Fatal(createDirErr)
		}
	}
	// ghi log vào file
	currentTimeStr := time.Now().Format("20060102")
	filePath := fmt.Sprintf("./logs/initializer-log-%s.log", currentTimeStr)
	// open file với chế độ rw, nếu file chưa tồn tại thì tạo
	file, err := os.OpenFile(filePath, os.O_RDWR|os.O_CREATE|os.O_APPEND, os.ModePerm)
	if err != nil {
		log.Fatal(err)
	}
	// add a file to zapcore
	syncFile := zapcore.AddSync(file)
	syncConsole := zapcore.AddSync(os.Stderr)
	return zapcore.NewMultiWriteSyncer(syncConsole, syncFile)
}
