package initializers

import (
	"be-gogin/global"
	"be-gogin/global/enums"
	"be-gogin/global/types"
	"be-gogin/internal/databases/postgres_sqlc"
	"be-gogin/internal/databases/tracers"
	"be-gogin/internal/loggers"
	"be-gogin/pkg/configs"
	"be-gogin/pkg/utils"
	"fmt"
	"net/url"
	"time"

	"github.com/jackc/pgx/v5/pgxpool"
	"go.uber.org/zap"
)

const InitPostgresPgxLogTopic = "Postgres-Pgx"

type PostgresSqlcConnection struct {
	logger         *zap.Logger
	PostgresMaster *types.PostgresSqlcMaster
	PostgresSlave  *types.PostgresSqlcSlave
}

// NewPostgresSqlcConnection hàm tạo kết nối đến Postgres
func NewPostgresSqlcConnection(logger *zap.Logger, postgresMaster *types.PostgresSqlcMaster, postgresSlave *types.PostgresSqlcSlave) *PostgresSqlcConnection {
	return &PostgresSqlcConnection{
		logger:         logger,
		PostgresMaster: postgresMaster,
		PostgresSlave:  postgresSlave,
	}
}

func NewSqlcPostgresMaster(tracer *tracers.SqlQueryTracer) *types.PostgresSqlcMaster {
	pool := InitializeSqlcPostgresNode(enums.PGMasterNode, tracer)
	return &types.PostgresSqlcMaster{
		Queries: postgres_sqlc.New(pool),
		Pool:    pool,
	}

}

func NewSqlcPostgresSlave(tracer *tracers.SqlQueryTracer) *types.PostgresSqlcSlave {
	pool := InitializeSqlcPostgresNode(enums.PGSlaveNode, tracer)
	return &types.PostgresSqlcSlave{
		Queries: postgres_sqlc.New(pool),
		Pool:    pool,
	}
}

func InitializeSqlcPostgresNode(nodeType enums.PostgresNodeType, tracer *tracers.SqlQueryTracer) *pgxpool.Pool {
	postgresConfig := global.AppConfig.Databases.Postgres
	var nodeConfig configs.PostgresNodeConfig
	if nodeType == enums.PGMasterNode {
		nodeConfig = postgresConfig.Master
	} else if nodeType == enums.PGSlaveNode {
		nodeConfig = postgresConfig.Slave
	} else {
		return nil
	}
	connectionString := fmt.Sprintf("postgresql://%s:%s@%s:%d/%s",
		nodeConfig.User, url.QueryEscape(nodeConfig.Password), nodeConfig.Host, nodeConfig.Port, nodeConfig.Database) + utils.IIf(nodeConfig.SSLRequired, "?sslmode=verify-full", "")
	// cấu hình pooling với drvier pgx
	poolConfig, err := pgxpool.ParseConfig(connectionString)
	poolConfig.MaxConns = int32(nodeConfig.MaxOpenConnections)
	poolConfig.MaxConnLifetime = time.Duration(nodeConfig.ConnectionMaxLifetimeMs) * time.Millisecond
	poolConfig.MaxConnIdleTime = time.Duration(nodeConfig.MaxIdleConnections)
	poolConfig.ConnConfig.Tracer = tracer
	// tạo connection
	pool, err := pgxpool.NewWithConfig(global.RootBackGroundContext, poolConfig)
	nodeName := utils.IIf(nodeType == enums.PGMasterNode, "Master", "Slave")
	if err != nil {
		loggers.Error(InitPostgresPgxLogTopic, err, fmt.Sprintf("Failed to connect to Postgres %s DB with SQLC", nodeName))
		return nil
	} else {
		loggers.Info(InitPostgresPgxLogTopic, fmt.Sprintf("Connected to Postgres %s DB with SQLC and initialized successfully", nodeName))
		return pool
	}
}

// CleanUpPostgresSqlcConnection Cleanup function để đóng database connections
func (pg *PostgresSqlcConnection) CleanUpPostgresSqlcConnection() {
	masterPool := pg.PostgresMaster.Pool
	masterPool.Close()
	loggers.Info(InitPostgresPgxLogTopic, "Closed Postgres Master DB SQLC Connection")
	// close slave pool
	slavePool := pg.PostgresSlave.Pool
	slavePool.Close()
	loggers.Info(InitPostgresPgxLogTopic, "Closed Postgres Slave DB SQLC Connection")
}
