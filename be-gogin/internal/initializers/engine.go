package initializers

import (
	"be-gogin/global"
	"be-gogin/global/enums"

	"github.com/gin-gonic/gin"
)

func InitEngine(engine *gin.Engine) {
	if global.AppConfig.Server.Mode == string(enums.Local) || global.AppConfig.Server.Mode == string(enums.Development) {
		gin.SetMode(gin.DebugMode)
		gin.ForceConsoleColor()
		engine = gin.Default()
	} else {
		gin.SetMode(gin.ReleaseMode)
		engine = gin.New()
	}
}
