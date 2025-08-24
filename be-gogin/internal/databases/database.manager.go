package databases

import (
	"be-gogin/internal/databases/postgres_sqlc"
	"be-gogin/internal/initializers"

	"github.com/jackc/pgx/v5/pgxpool"
	"gorm.io/gorm"
)

type DatabaseManager struct {
	pgDBGorm    *initializers.PostgresGormConnection
	pgDBQueries *initializers.PostgresSqlcConnection
}

func NewDatabaseManager(
	pgDBGorm *initializers.PostgresGormConnection,
	pgDBQueries *initializers.PostgresSqlcConnection,
) *DatabaseManager {
	return &DatabaseManager{
		pgDBGorm:    pgDBGorm,
		pgDBQueries: pgDBQueries,
	}
}

func (dbManger *DatabaseManager) GetPgGormMaster() *gorm.DB {
	return dbManger.pgDBGorm.PostgresMaster
}
func (dbManger *DatabaseManager) GetPgGormSlave() *gorm.DB {
	return dbManger.pgDBGorm.PostgresSlave
}

func (dbManger *DatabaseManager) GetPgxPoolMaster() *pgxpool.Pool {
	return dbManger.pgDBQueries.PostgresMaster.Pool
}

func (dbManger *DatabaseManager) GetPgxQueriesMaster() *postgres_sqlc.Queries {
	return dbManger.pgDBQueries.PostgresMaster.Queries
}

func (dbManger *DatabaseManager) GetPgxPoolSlave() *pgxpool.Pool {
	return dbManger.pgDBQueries.PostgresSlave.Pool
}

func (dbManger *DatabaseManager) GetPgxQueriesSlave() *postgres_sqlc.Queries {
	return dbManger.pgDBQueries.PostgresSlave.Queries
}
