package middlewares

import (
	"be-gogin/global"
	"time"

	"github.com/gin-gonic/gin"
)

const startHandlerLogFormat = "=====ENTERED: [%s] ====="
const endHandlerLogFormat = "=====ENDED: [%s] after [%v]ms ====="

func HandleLogging() gin.HandlerFunc {
	return func(context *gin.Context) {
		now := time.Now()
		global.Logger.Sugar().Infof(startHandlerLogFormat, context.FullPath())
		context.Next()
		global.Logger.Sugar().Infof(endHandlerLogFormat, context.FullPath(), time.Since(now).Milliseconds())
	}
}
