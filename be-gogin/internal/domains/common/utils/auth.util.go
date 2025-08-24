package utils

import (
	"be-gogin/global/constants"
	"be-gogin/internal/domains/auth/dtos"
	"be-gogin/internal/domains/role/entities"
	"github.com/gin-gonic/gin"
	"github.com/samber/lo"
)

const currentUserContextKey = "current-user"

func SetSecurityContext(c *gin.Context, userDetail dtos.UserDetails) {
	c.Set(currentUserContextKey, userDetail)
}

// GetCurrentUser lấy thông tin user đã đăng nhập/**
func GetCurrentUser(c *gin.Context) dtos.UserDetails {
	userDetail, exists := c.Get(currentUserContextKey)
	if exists {
		return userDetail.(dtos.UserDetails)
	} else {
		return dtos.UserDetails{}
	}
}

// GetCurrentUserId lấy id user đăng nhập/**
func GetCurrentUserId(c *gin.Context) int64 {
	userDetail, exists := c.Get(currentUserContextKey)
	if exists {
		return int64(userDetail.(dtos.UserDetails).Id)
	} else {
		return 0
	}
}

// GetCurrentUsername lấy id user đăng nhập/**
func GetCurrentUsername(c *gin.Context) string {
	userDetail, exists := c.Get(currentUserContextKey)
	if exists {
		return userDetail.(dtos.UserDetails).Username
	} else {
		return constants.EmptyChar
	}
}

// GetCurrentUserRoles lấy id user đăng nhập/**
func GetCurrentUserRoles(c *gin.Context) []string {
	userDetail, exists := c.Get(currentUserContextKey)
	if exists {
		return lo.Map(userDetail.(dtos.UserDetails).Roles, func(item entities.Role, index int) string {
			return item.Code
		})
	} else {
		return []string{}
	}
}
