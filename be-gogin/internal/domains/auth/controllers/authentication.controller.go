package controllers

import (
	"be-gogin/global/constants"
	"be-gogin/internal/domains/auth/dtos"
	"be-gogin/internal/domains/auth/services"
	internalerrors "be-gogin/internal/errors"
	"be-gogin/pkg/utils"
	"github.com/gin-gonic/gin"
	"net/http"
)

type AuthenticationController struct {
	authenticationService services.IAuthenticationService
}

func NewAuthenticationController(
	authenticationService services.IAuthenticationService,
) *AuthenticationController {
	return &AuthenticationController{
		authenticationService: authenticationService,
	}
}

// Login Đăng nhập
func (auth *AuthenticationController) Login(context *gin.Context) {
	defer utils.LogFunction()()
	loginRequest := context.MustGet(constants.RequestBodyContextKey).(dtos.LoginRequestDTO)
	response, loginErr := auth.authenticationService.Login(loginRequest, context)
	if loginErr != nil {
		internalerrors.HandleError(context, loginErr)
		return
	}
	context.JSON(http.StatusOK, response)
}

// RefreshToken Cấp lại access token
func (auth *AuthenticationController) RefreshToken(context *gin.Context) {
	header := context.MustGet(constants.RequestHeaderContextKey).(dtos.RefreshTokenRequestHeader)
	response, refreshErr := auth.authenticationService.RefreshToken(header.RefreshToken, context)
	if refreshErr != nil {
		internalerrors.HandleError(context, refreshErr)
		return
	}
	context.JSON(http.StatusOK, response)
}

// Logout Đăng xuất
func (auth *AuthenticationController) Logout(context *gin.Context) {
	logoutRequest := context.MustGet(constants.RequestBodyContextKey).(dtos.LogoutRequestDTO)
	logoutErr := auth.authenticationService.Logout(logoutRequest, context)
	if logoutErr != nil {
		internalerrors.HandleError(context, logoutErr)
		return
	}
	context.Status(http.StatusOK)
}
