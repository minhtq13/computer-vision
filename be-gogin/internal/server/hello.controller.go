package server

import (
	"net/http"

	"github.com/gin-gonic/gin"
)

type HelloController struct{}

func NewHelloController() *HelloController {
	return &HelloController{}
}

func (helloController *HelloController) Hello(context *gin.Context) {
	context.JSON(http.StatusOK, gin.H{
		"message": "Hello from Go Gin ELS",
		"status":  "success",
	})
}
