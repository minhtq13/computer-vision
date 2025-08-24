package middlewares

import (
	"be-gogin/global"
	"time"

	"github.com/gin-contrib/cors"
	"github.com/gin-gonic/gin"
)

func CorsMiddleware() gin.HandlerFunc {
	allowsOrigins := global.AppConfig.Server.AllowedOrigins
	allowsMethods := []string{"GET", "POST", "PUT", "DELETE", "PATCH", "OPTIONS"}
	allowsHeaders := []string{
		"Origin",
		"Content-Length",
		"Content-Type",
		"Authorization",
		"X-Requested-With",
		"X-API-Key",
		"X-Auth-Token",
		"refreshToken",
	}
	exposedHeaders := []string{"X-Total-Count", "X-Auth-Token"}

	// Tạo cấu hình cors và return cors handler function to gin
	return cors.New(cors.Config{
		AllowOrigins:     allowsOrigins,
		AllowMethods:     allowsMethods,
		AllowHeaders:     allowsHeaders,
		ExposeHeaders:    exposedHeaders,
		AllowCredentials: true,
		AllowWebSockets:  true,
		MaxAge:           24 * time.Hour,
	})
}
