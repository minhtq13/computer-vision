package types

import (
	"be-gogin/internal/databases/postgres_sqlc"

	"github.com/jackc/pgx/v5/pgxpool"
	"gorm.io/gorm"
)

type PostgresGormMaster *gorm.DB
type PostgresGormSlave *gorm.DB

type PostgresSqlcMaster struct {
	Queries *postgres_sqlc.Queries
	Pool    *pgxpool.Pool
}
type PostgresSqlcSlave struct {
	Queries *postgres_sqlc.Queries
	Pool    *pgxpool.Pool
}
