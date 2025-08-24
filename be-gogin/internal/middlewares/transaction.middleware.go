package middlewares

import (
	"be-gogin/global"
	"be-gogin/global/constants"
	"be-gogin/internal/databases"
	"be-gogin/internal/errors"
	"net/http"
	"strings"

	"github.com/gin-gonic/gin"
)

func Transactional(dbManager *databases.DatabaseManager) gin.HandlerFunc {
	return func(ginCtx *gin.Context) {
		ctx := ginCtx.Request.Context()

		// Khởi tạo các transaction cho master connection (slave = read-only nên không cần)
		gormTx := dbManager.GetPgGormMaster().Begin()
		pgxTx, pgxTransactionErr := dbManager.GetPgxPoolMaster().Begin(ctx)
		if gormTx.Error != nil || pgxTransactionErr != nil {
			errors.LogError(gormTx.Error)
			errors.LogError(pgxTransactionErr)
			ginCtx.AbortWithStatusJSON(http.StatusInternalServerError, errors.ThrowInternalServerError(
				strings.Join([]string{gormTx.Error.Error(), pgxTransactionErr.Error()}, ";"), ginCtx.FullPath()))
			return
		}

		// tạo transaction manager và set vào context để quản lý transaction trong handler
		tx := &databases.TransactionManager{
			PgGormTx:    gormTx,
			PgQueriesTx: pgxTx,
		}
		ginCtx.Set(constants.TransactionContextKey, tx)

		// Xử lý commit hoặc rollback transaction
		defer func() {
			// case recover
			needRollback := false
			if r := recover(); r != nil {
				needRollback = true
			} else if ginCtx.Writer.Status() >= http.StatusBadRequest { // error dạng lỗi logic xử lý request (status >= 400)
				needRollback = true
			} else if len(ginCtx.Errors.Errors()) > 0 {
				needRollback = true
			}

			// Kiểm tra điều nếu có lỗi thì rollback không thì thực hiện commit transaction
			if needRollback {
				gormRollbackErr := gormTx.Rollback().Error
				pgxRollBackErr := pgxTx.Rollback(ctx)
				if gormRollbackErr != nil || pgxRollBackErr != nil {
					global.Logger.Sugar().Errorf("======== Rolled back transaction error %s =======",
						strings.Join([]string{gormRollbackErr.Error(), pgxRollBackErr.Error()}, ";"))
					errors.LogError(gormRollbackErr)
					errors.LogError(pgxRollBackErr)
					return
				} else {
					global.Logger.Info("======== Rolled back transaction =======")
				}
			} else {
				gormCommitErr := gormTx.Commit().Error
				pgxCommitErr := pgxTx.Commit(ctx)
				if gormCommitErr != nil || pgxCommitErr != nil {
					global.Logger.Sugar().Errorf("======== Commited transaction error %s =======",
						strings.Join([]string{gormCommitErr.Error(), pgxCommitErr.Error()}, ";"))
					errors.LogError(gormCommitErr)
					errors.LogError(pgxCommitErr)
					return
				} else {
					global.Logger.Info("======== Commited transaction =======")
				}
			}
		}()

		// Tiếp tục forward đến các handlers
		ginCtx.Next()
	}
}
