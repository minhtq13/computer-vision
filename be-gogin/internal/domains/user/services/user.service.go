package services

import (
	"be-gogin/internal/domains/user/dtos"
	"github.com/gin-gonic/gin"
)

// IUserService UserService sử dụng interface
type IUserService interface {
	GetProfile(c *gin.Context) (dtos.ProfileUserDTO, error)
}
