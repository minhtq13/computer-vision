package initializers

import (
	"be-gogin/global"
	"be-gogin/global/types"
	"be-gogin/internal/domains/user/entities"
	"be-gogin/internal/loggers"
	"be-gogin/pkg/configs"
	"be-gogin/pkg/utils"
	"context"
	"fmt"
	"time"

	"go.uber.org/zap"
	"gorm.io/driver/postgres"
	"gorm.io/gorm"
	"gorm.io/gorm/logger"
)

const InitPostgresGormLogTopic = "Postgres-Gorm"

type PostgresGormConnection struct {
	logger         *zap.Logger
	PostgresMaster *gorm.DB
	PostgresSlave  *gorm.DB
}

// NewPostgresGormConnection hàm tạo base repositories
func NewPostgresGormConnection(logger *zap.Logger, postgresMaster types.PostgresGormMaster, postgresSlave types.PostgresGormSlave) *PostgresGormConnection {
	return &PostgresGormConnection{
		logger:         logger,
		PostgresMaster: postgresMaster,
		PostgresSlave:  postgresSlave,
	}
}

func NewPostgresGormMaster(gormLogger logger.Interface) types.PostgresGormMaster {
	configMaster := global.AppConfig.Databases.Postgres.Master
	sslMode := utils.IIf(configMaster.SSLRequired, "require", "disable")
	dsnPattern := "host=%s port=%v user=%s password=%s dbname=%s sslmode=%s TimeZone=%s"
	dsn := fmt.Sprintf(dsnPattern, configMaster.Host, configMaster.Port, configMaster.User, configMaster.Password, configMaster.Database,
		sslMode, configMaster.TimeZone)
	// tạo config
	gormConfig := &gorm.Config{
		SkipDefaultTransaction: false,
		Logger:                 gormLogger,
		NowFunc: func() time.Time {
			location, _ := time.LoadLocation(configMaster.TimeZone)
			return time.Now().In(location)
		},
	}
	db, err := gorm.Open(postgres.New(postgres.Config{
		DSN:                  dsn,
		PreferSimpleProtocol: true,
	}), gormConfig)
	// tạo context kết nối với timeout 20s
	_, cancel := context.WithTimeout(global.RootBackGroundContext, 20*time.Second)
	defer cancel()
	if err != nil {
		loggers.Error(InitPostgresGormLogTopic, err, "Failed to connect to Postgres Master DB with Gorm")
		return nil
	} else {
		loggers.Info(InitPostgresGormLogTopic, "Connected to Postgres Master DB with Gorm and initialized successfully")
		// config pooling
		SetPoolMaster(db, global.AppConfig.Databases.Postgres)
		SetupJoinTable(db)
		return db
	}
}

func NewPostgresGormSlave(gormLogger logger.Interface) types.PostgresGormSlave {
	configSlave := global.AppConfig.Databases.Postgres.Slave
	sslMode := utils.IIf(configSlave.SSLRequired, "require", "disable")
	dsnPattern := "host=%s port=%v user=%s password=%s dbname=%s sslmode=%s TimeZone=%s"
	dsn := fmt.Sprintf(dsnPattern, configSlave.Host, configSlave.Port, configSlave.User, configSlave.Password, configSlave.Database,
		sslMode, configSlave.TimeZone)
	// tạo gorm config
	gormConfig := &gorm.Config{
		SkipDefaultTransaction: false,
		Logger:                 gormLogger,
		NowFunc: func() time.Time {
			location, _ := time.LoadLocation(configSlave.TimeZone)
			return time.Now().In(location)
		},
	}
	db, err := gorm.Open(postgres.New(postgres.Config{
		DSN:                  dsn,
		PreferSimpleProtocol: true,
	}), gormConfig)
	// tạo context kết nối với timeout 10s
	_, cancel := context.WithTimeout(global.RootBackGroundContext, 10*time.Second)
	defer cancel()
	if err != nil {
		loggers.Error(InitPostgresGormLogTopic, err, "Failed to connect to Postgres Slave DB with Gorm")
		return nil
	} else {
		loggers.Info(InitPostgresGormLogTopic, "Connected to Postgres Slave DB with Gorm and initialized successfully")
		// config pooling
		SetPoolSlave(db, global.AppConfig.Databases.Postgres)
		SetupJoinTable(db)
		return db
	}
}

// SetPoolMaster Set-up connection pool for master database/**
func SetPoolMaster(masterDB *gorm.DB, config configs.PostgresConfig) {
	db, err := masterDB.DB()
	if err != nil {
		loggers.Error(InitPostgresGormLogTopic, err, "Failed to get Postgres DB connection with Gorm")
		return
	}
	db.SetConnMaxIdleTime(time.Duration(config.Master.MaxIdleConnections))
	db.SetMaxIdleConns(config.Master.MaxIdleConnections)
	db.SetMaxOpenConns(config.Master.MaxOpenConnections)
	connMaxLifetime := time.Duration(config.Master.ConnectionMaxLifetimeMs) * time.Millisecond
	db.SetConnMaxLifetime(connMaxLifetime)
}

// SetPoolSlave SetPoolMaster Set-up connection pool for master database/**
func SetPoolSlave(slaveDB *gorm.DB, config configs.PostgresConfig) {
	db, err := slaveDB.DB()
	if err != nil {
		loggers.Error(InitPostgresGormLogTopic, err, "Failed to get Postgres DB connection with Gorm")
		return
	}
	db.SetConnMaxIdleTime(time.Duration(config.Master.MaxIdleConnections))
	db.SetMaxIdleConns(config.Master.MaxIdleConnections)
	db.SetMaxOpenConns(config.Master.MaxOpenConnections)
	connMaxLifetime := time.Duration(config.Master.ConnectionMaxLifetimeMs) * time.Millisecond
	db.SetConnMaxLifetime(connMaxLifetime)
}

// SetupJoinTable Hàm khai báo set up các mapping dạng many to many/ many to one/ .../**
func SetupJoinTable(db *gorm.DB) {
	// users_roles
	err1 := db.SetupJoinTable(&entities.UserEntity{}, "Roles", &entities.UserRole{})
	if err1 != nil {
	}
	// users_departments
	err2 := db.SetupJoinTable(&entities.UserEntity{}, "Departments", &entities.UserDepartment{})
	if err2 != nil {
	}
}

// CleanUpPostgresGormConnection Cleanup function để đóng database connections
func CleanUpPostgresGormConnection(postgresConnection *PostgresGormConnection) {
	dbMaster, errMaster := postgresConnection.PostgresMaster.DB()
	if errMaster == nil {
		err := dbMaster.Close()
		if err != nil {
			loggers.Error(InitPostgresGormLogTopic, err, "Postgres Master DB Gorm disconnection error")
			return
		}
		loggers.Info(InitPostgresGormLogTopic, "Closed Postgres Master DB Gorm Connection")
	} else {
		loggers.Error(InitPostgresGormLogTopic, errMaster, "Postgres Master DB Gorm disconnection error")
	}
	dbSlave, errSlave := postgresConnection.PostgresSlave.DB()
	if errSlave == nil {
		err := dbSlave.Close()
		if err != nil {
			loggers.Error(InitPostgresGormLogTopic, err, "Postgres Slave DB Gorm disconnection error")
			return
		}
		loggers.Info(InitPostgresGormLogTopic, "Closed Postgres Slave DB Gorm Connection")
	} else {
		loggers.Error(InitPostgresGormLogTopic, errMaster, "Postgres Slave DB Gorm disconnection error")
	}
}
