package services

import (
	"be-gogin/internal/domains/auth/dtos"
	"github.com/gin-gonic/gin"
)

type IAuthenticationService interface {
	Login(loginRequest dtos.LoginRequestDTO, c *gin.Context) (dtos.LoginResponseDTO, error)

	RefreshToken(refreshToken string, c *gin.Context) (dtos.RefreshTokenResDTO, error)

	Logout(logoutRequest dtos.LogoutRequestDTO, c *gin.Context) error

	VerifyToken(token string, c *gin.Context) (bool, error)
}
