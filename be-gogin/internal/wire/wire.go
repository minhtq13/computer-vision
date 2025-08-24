//wire.go
//go:build wireinject
// +build wireinject

package wire

import (
	"be-gogin/internal/databases"
	"be-gogin/internal/databases/repository"
	"be-gogin/internal/databases/tracers"
	controllers2 "be-gogin/internal/domains/auth/controllers"
	authrepos "be-gogin/internal/domains/auth/repositories/postgres"
	authserviceimpls "be-gogin/internal/domains/auth/services/impls"
	controllers3 "be-gogin/internal/domains/messaging/controllers"
	controllers4 "be-gogin/internal/domains/notification/controllers"
	notiimpls "be-gogin/internal/domains/notification/services/impls"
	"be-gogin/internal/domains/user/controllers"
	userrepos "be-gogin/internal/domains/user/repositories/postgres"
	"be-gogin/internal/domains/user/services/impls"
	"be-gogin/internal/initializers"
	"be-gogin/internal/messaging/confluent"
	"be-gogin/internal/messaging/handlers"
	"be-gogin/internal/messaging/segmenio"
	"be-gogin/internal/middlewares"
	rootrouters "be-gogin/internal/routers"
	"be-gogin/internal/server"

	"github.com/google/wire"
)

// Set of Postgres Repository
var PGRepositoryProvidedSet = wire.NewSet(
	repository.NewCommonPGRepository,
	wire.Bind(new(repository.IRepository), new(*repository.CommonPGRepository)),
	userrepos.NewUserPGRepository,
	authrepos.NewAuthInfoPGRepository)

var ServiceProvidedSet = wire.NewSet(authserviceimpls.NewAuthenticationService, impls.NewUserService, notiimpls.NewNotificationService)

var MiddlewareProviderSet = wire.NewSet(middlewares.NewAuthenticationMiddleware)
var ControllerProvidedSet = wire.NewSet(
	controllers.NewUserController,
	controllers2.NewAuthenticationController,
	controllers3.NewMessagingController,
	controllers4.NewNotificationController)
var PrivateRouterProvidedSet = wire.NewSet(rootrouters.NewPrivateRouterGroup)
var PublicRouterProvidedSet = wire.NewSet(rootrouters.NewPublicRouterGroup)
var KafkaMessageHandlerProvidedSet = wire.NewSet(handlers.NewLogKafkaMessageHandler)

func InitializeServer() (*server.Server, error) {
	wire.Build(
		// initializers
		initializers.NewAppConfig,
		initializers.NewLogger,
		tracers.NewSqlQueryTracer,
		tracers.NewGormLogger,
		initializers.NewPostgresGormMaster,
		initializers.NewPostgresGormSlave,
		initializers.NewPostgresGormConnection,
		initializers.NewSqlcPostgresMaster,
		initializers.NewSqlcPostgresSlave,
		initializers.NewPostgresSqlcConnection,
		databases.NewDatabaseManager,
		initializers.NewRedisConnection,
		initializers.NewMongoConnection,
		segmenio.NewKafkaSegIOProducer,
		segmenio.NewKafkaSegIOConsumer,
		confluent.NewKafkaConfluentProducerManager,
		confluent.NewKafkaConfluentConsumerManager,
		initializers.NewFirebase,
		// core components in internal
		PGRepositoryProvidedSet,
		ServiceProvidedSet,
		MiddlewareProviderSet,
		ControllerProvidedSet,
		KafkaMessageHandlerProvidedSet,
		PrivateRouterProvidedSet,
		PublicRouterProvidedSet,
		rootrouters.NewRootRouterGroup,
		// server and sample controller
		server.NewHelloController,
		server.NewServer,
	)
	return new(server.Server), nil
}
