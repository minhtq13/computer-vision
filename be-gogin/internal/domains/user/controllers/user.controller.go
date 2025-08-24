package controllers

import (
	"be-gogin/internal/domains/user/services"
	"be-gogin/internal/errors"
	"be-gogin/pkg/utils"
	"github.com/gin-gonic/gin"
	"net/http"
)

// UserController Dependency Injection
type UserController struct {
	userService services.IUserService
}

func NewUserController(userService services.IUserService) *UserController {
	return &UserController{
		userService: userService,
	}
}

/**
Implementation
*/

func (userController *UserController) GetProfile(context *gin.Context) {
	defer utils.LogFunction()()
	data, err := userController.userService.GetProfile(context)
	if err != nil {
		errors.HandleError(context, err)
		return
	}
	context.JSON(http.StatusOK, data)
}
