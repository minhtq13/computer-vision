package databases

import (
	"be-gogin/global/constants"

	"github.com/gin-gonic/gin"
	"github.com/jackc/pgx/v5"
	"gorm.io/gorm"
)

type TransactionManager struct {
	PgGormTx    *gorm.DB
	PgQueriesTx pgx.Tx
}

func GetTx(c *gin.Context) *TransactionManager {
	if tx, ok := c.Get(constants.TransactionContextKey); ok {
		return tx.(*TransactionManager)
	}
	return nil
}
