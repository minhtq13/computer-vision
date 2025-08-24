package server

import (
	"be-gogin/global"
	"be-gogin/global/constants"
	"be-gogin/internal/databases"
	"be-gogin/internal/databases/tracers"
	controllers2 "be-gogin/internal/domains/messaging/controllers"
	controllers3 "be-gogin/internal/domains/notification/controllers"
	"be-gogin/internal/domains/user/controllers"
	"be-gogin/internal/errors"
	"be-gogin/internal/initializers"
	"be-gogin/internal/loggers"
	"be-gogin/internal/messaging/confluent"
	"be-gogin/internal/messaging/handlers"
	"be-gogin/internal/messaging/segmenio"
	"be-gogin/internal/middlewares"
	"be-gogin/internal/routers"
	"be-gogin/pkg/configs"
	"be-gogin/pkg/utils"
	"context"
	errors2 "errors"
	"fmt"
	"net/http"
	"os"
	"os/signal"
	"path/filepath"
	"strconv"
	"syscall"
	"time"

	"github.com/gin-contrib/static"
	"github.com/gin-gonic/gin"
	"github.com/redis/go-redis/v9"
	"go.mongodb.org/mongo-driver/v2/mongo"
	"go.uber.org/zap"
)

const InitServerLogTopic = "Server"

type Server struct {
	// config and initializers
	engine                 *gin.Engine
	AppConfig              *configs.AppEnvConfig
	Logger                 *zap.Logger
	PostgresSqlcTracer     *tracers.SqlQueryTracer
	PostgresGormConnection *initializers.PostgresGormConnection
	PostgresSqlcConnection *initializers.PostgresSqlcConnection
	databaseManager        *databases.DatabaseManager
	RedisConnection        *redis.Client
	MongoConnection        *mongo.Client
	// segmentio kakfa
	kafkaSegIOProducer *segmenio.KafkaSegIOProducer
	kafkaSegIOConsumer *segmenio.KafkaSegIOConsumer
	// confluent kafka
	kafkaConfluentProducerManager *confluent.KafkaConfluentProducerManager
	kafkaConfluentConsumerManager *confluent.KafkaConfluentConsumerManager
	firebase                      *initializers.Firebase
	// controllers
	helloController         *HelloController
	userController          *controllers.UserController
	messagingController     *controllers2.MessagingController
	notificationsController *controllers3.NotificationController
	// Kafka message handlers
	logKafkaMessageHandler *handlers.LogKafkaMessageHandler
	// middlewares
	authenticationMiddleware *middlewares.AuthenticationMiddleware
	// routers
	rootRouterGroup *routers.RootRouterGroup
}

func NewServer(
	// config and initializer
	appConfig *configs.AppEnvConfig,
	logger *zap.Logger,
	postgresSqlcTracer *tracers.SqlQueryTracer,
	postgresGormConnection *initializers.PostgresGormConnection,
	postgresSqlcConnection *initializers.PostgresSqlcConnection,
	databaseManager *databases.DatabaseManager,
	redisConnection *redis.Client,
	mongoConnection *mongo.Client,
	// segmentio kakfa
	kafkaSegIOProducer *segmenio.KafkaSegIOProducer,
	kafkaSegIOConsumer *segmenio.KafkaSegIOConsumer,
	// confluent kafka
	kafkaConfluentProducerManager *confluent.KafkaConfluentProducerManager,
	kafkaConfluentConsumerManager *confluent.KafkaConfluentConsumerManager,
	firebase *initializers.Firebase,
	// controllers
	helloController *HelloController,
	userController *controllers.UserController,
	messagingController *controllers2.MessagingController,
	notificationsController *controllers3.NotificationController,
	// Kafka message handlers
	logKafkaMessageHandler *handlers.LogKafkaMessageHandler,

	authenticationMiddleware *middlewares.AuthenticationMiddleware,
	rootRouterGroup *routers.RootRouterGroup) *Server {
	return &Server{
		// config and initializers
		engine:                        gin.Default(),
		AppConfig:                     appConfig,
		Logger:                        logger,
		PostgresGormConnection:        postgresGormConnection,
		PostgresSqlcConnection:        postgresSqlcConnection,
		databaseManager:               databaseManager,
		RedisConnection:               redisConnection,
		MongoConnection:               mongoConnection,
		PostgresSqlcTracer:            postgresSqlcTracer,
		kafkaSegIOProducer:            kafkaSegIOProducer,
		kafkaSegIOConsumer:            kafkaSegIOConsumer,
		kafkaConfluentProducerManager: kafkaConfluentProducerManager,
		kafkaConfluentConsumerManager: kafkaConfluentConsumerManager,
		firebase:                      firebase,
		// controllers
		helloController:         helloController,
		messagingController:     messagingController,
		userController:          userController,
		notificationsController: notificationsController,
		// kafka message handlers
		logKafkaMessageHandler: logKafkaMessageHandler,
		// middlewares
		authenticationMiddleware: authenticationMiddleware,
		rootRouterGroup:          rootRouterGroup,
	}
}

func (server *Server) InitRouter() {
	// config the root router
	// config router from root
	rootGroup := server.engine.Group(constants.ServerDefaultPrefix)
	{
		// declare global middleware for the root router group
		rootGroup.Use(middlewares.HandleLogging())
		// other routers from root
		rootGroup.GET("/api/hello", server.helloController.Hello)

		// router for static resources (upload, static files)
		rootGroup.StaticFS("/resources", static.LocalFile("./resources", false))

		// router for shared resources (between services)
		rootGroup.StaticFS("/public/shared", static.LocalFile(filepath.Clean(utils.GetSharedDataDirectory()), false))

	}
	{
		// config router for private apis
		server.rootRouterGroup.PrivateRouterGroup.InitPrivateRouter(rootGroup)
	}
	{
		// config router public apis
		server.rootRouterGroup.PublicRouterGroup.InitPublicRouter(rootGroup)
	}

	global.Logger.Info("======= Init routers successfully! =======")
}

func (server *Server) cleanUpConnections() {
	// Đóng các kết nối đến db/redis
	initializers.CleanUpPostgresGormConnection(server.PostgresGormConnection)
	initializers.CleanUpRedisConnection(server.RedisConnection)
	server.PostgresSqlcConnection.CleanUpPostgresSqlcConnection()

	kafkaConfig := server.AppConfig.Kafka
	// Close kafka writers/readers in producer/consumer (from segmentio)
	closeProducerErr := server.kafkaSegIOProducer.Close()
	if closeProducerErr != nil {
		errors.LogError(closeProducerErr)
		return
	}
	if kafkaConfig.Options.Enabled && server.kafkaSegIOConsumer != nil {
		closeConsumerErr := server.kafkaSegIOConsumer.Close()
		if closeConsumerErr != nil {
			errors.LogError(closeConsumerErr)
			return
		}
	}
	// Init kafka producer (from confluent)
	server.kafkaConfluentProducerManager.Close()
}

func (server *Server) Run() {
	// Init gin engine
	initializers.InitEngine(server.engine)
	// Load api authorization config
	initializers.InitApiAuthorizationConfig()
	// Use middleware to config CORS
	server.engine.Use(middlewares.CorsMiddleware())
	// Init router from gin engine
	server.InitRouter()

	// start/defer close consuming kafka topics
	kafkaConfig := server.AppConfig.Kafka
	if kafkaConfig.Options.Enabled {
		server.kafkaConfluentConsumerManager.Subscribe(global.RootBackGroundContext, kafkaConfig.Producer.DefaultTopic, server.logKafkaMessageHandler)
	}

	// Init directories (shared directory, logs, ...)
	initializers.InitDirectories()

	// Run server
	port := global.AppConfig.Server.Port
	loggers.Info(InitServerLogTopic, fmt.Sprintf("GoGin server is starting on port %d with GIN_MODE %s", port, os.Getenv("GIN_MODE")))
	httpServer := &http.Server{
		Addr:    ":" + strconv.FormatInt(int64(port), 10),
		Handler: server.engine,
	}

	// run server another go routine
	go func() {
		if err := httpServer.ListenAndServe(); err != nil && errors2.Is(err, http.ErrServerClosed) {
			loggers.Error(InitServerLogTopic, err, "Run Gogin server error")
		} else {
			loggers.Info(InitServerLogTopic, fmt.Sprintf("GoGin server is running on port %d with GIN_MODE %s", global.AppConfig.Server.Port, os.Getenv("GIN_MODE")))
		}
	}()

	// Lắng nghe tín hiệu shutdown -> graceful-shutdown
	quit := make(chan os.Signal, 1)
	signal.Notify(quit, syscall.SIGINT, syscall.SIGTERM)
	<-quit
	global.Logger.Info("Shutting down server...")

	// Tạo context timeout để shutdown server trong 10s
	shutdownCtx, cancel := context.WithTimeout(global.RootBackGroundContext, 15*time.Second)
	defer cancel()
	defer server.cleanUpConnections()

	// Hàm shutdown được gọi -> ngừng nhận requests mới và hoàn thành các request hiện tại
	if err := httpServer.Shutdown(shutdownCtx); err != nil {
		global.Logger.Fatal("Server forced to shutdown:", zap.Error(err))
	}
}
