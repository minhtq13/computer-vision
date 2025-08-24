package initializers

import (
	"be-gogin/global"
	"be-gogin/internal/loggers"
	"context"
	"fmt"

	"github.com/redis/go-redis/v9"
)

const InitRedisLogTopic = "Redis"

func NewRedisConnection() *redis.Client {
	redisConnection := InitRedis(global.RootBackGroundContext)
	return redisConnection
}

func InitRedis(ctx context.Context) *redis.Client {
	redisConfig := global.AppConfig.Redis
	rdb := redis.NewClient(&redis.Options{
		Addr:     fmt.Sprintf("%s:%d", redisConfig.Host, redisConfig.Port),
		Password: redisConfig.Password,
		DB:       redisConfig.DB,
		PoolSize: redisConfig.PoolSize,
	})

	_, err := rdb.Ping(ctx).Result()
	if err != nil {
		loggers.Error(InitRedisLogTopic, err, "Redis connection error")
		return nil
	} else {
		loggers.Info(InitRedisLogTopic, "Redis connection established successfully")
		return rdb
	}
}

func CleanUpRedisConnection(redisConnection *redis.Client) {
	err := redisConnection.Close()
	if err != nil {
		loggers.Error(InitRedisLogTopic, err, "Redis disconnection error")
		return
	}
	loggers.Info(InitRedisLogTopic, "Closed Redis Connection")
}
