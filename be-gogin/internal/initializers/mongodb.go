package initializers

import (
	"be-gogin/global"
	"be-gogin/internal/loggers"
	"context"
	"fmt"
	"net/url"
	"time"

	"go.mongodb.org/mongo-driver/v2/mongo"
	"go.mongodb.org/mongo-driver/v2/mongo/options"
	"go.mongodb.org/mongo-driver/v2/mongo/readpref"
)

const InitMongoLogTopic = "MongoDB"

func NewMongoConnection() *mongo.Client {
	mongoClient, _ := initMongoDB()
	return mongoClient
}

func initMongoDB() (*mongo.Client, error) {
	config := global.AppConfig.Databases.MongoDB
	// khởi tạo kết nối đến mongodb
	uri := fmt.Sprintf("mongodb://%s:%s@%s:%d/%s", url.QueryEscape(config.Username), url.QueryEscape(config.Password), config.Host, config.Port, config.Database)
	client, connectErr := mongo.Connect(options.Client().ApplyURI(uri))
	if connectErr != nil {
		loggers.Error(InitMongoLogTopic, connectErr, "MongoDB connection error")
		return nil, connectErr
	}
	// tạo context kết nối timeout 20s
	ctx, cancel := context.WithTimeout(global.RootBackGroundContext, 10*time.Second)
	defer cancel()
	if err := client.Ping(ctx, readpref.Primary()); err != nil {
		loggers.Error(InitMongoLogTopic, err, "MongoDB connection error")
		return nil, err
	}
	loggers.Info(InitMongoLogTopic, "MongoDB connection established successfully")
	return client, nil
}

func CleanUpMongoConnection(mongoConnection *mongo.Client) {
	err := mongoConnection.Disconnect(global.RootBackGroundContext)
	if err != nil {
		loggers.Error(InitMongoLogTopic, err, "MongoDB disconnection error")
		return
	}
	loggers.Info(InitMongoLogTopic, "Closed MongoDB Connection")
}
