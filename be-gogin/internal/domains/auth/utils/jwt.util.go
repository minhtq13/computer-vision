package utils

import (
	"be-gogin/global"
	"be-gogin/global/constants"
	"be-gogin/internal/domains/auth/dtos"
	"be-gogin/internal/domains/user/entities"
	customerrors "be-gogin/internal/errors"
	"be-gogin/pkg/configs"
	"encoding/base64"
	"fmt"
	"github.com/golang-jwt/jwt/v5"
	"strings"
	"time"
)

var mapSignAlgorithm = map[string]jwt.SigningMethod{
	"HS256": jwt.SigningMethodHS256,
	"HS384": jwt.SigningMethodHS384,
	"HS512": jwt.SigningMethodHS512,
}

// ExtractAndValidateTokenFormat Kiểm tra định dạng jwt /*
func ExtractAndValidateTokenFormat(token string) (string, bool, error) {
	tokenParts := strings.Split(token, " ")
	if len(tokenParts) != 2 {
		return "", false, &customerrors.UnauthorizedError{
			Message: "invalid token format",
			Code:    constants.ErrorCodeInvalidJwtToken,
		}
	} else {
		// Check input scheme xem có phải là Scheme cấu hình hay không
		if tokenParts[0] != constants.TokenScheme {
			return "", false, &customerrors.UnauthorizedError{
				Message: "invalid token scheme",
				Code:    constants.ErrorCodeInvalidJwtToken,
			}
		} else {
			return tokenParts[1], true, nil
		}
	}
}

// GenerateJwt generate jwt token
func GenerateJwt(user dtos.UserDetails, config configs.JwtConfig) (string, error) {
	// tạo claims
	claims := jwt.MapClaims{
		"username": user.Username,
		"sub":      user.Username,
		"iat":      time.Now().Unix(),
		"exp":      time.Now().Add(time.Duration(config.AccessTokenExpiredMs * int64(time.Millisecond))).Unix(),
	}
	// tạo token với claims và sign token với secret key
	token := jwt.NewWithClaims(mapSignAlgorithm[config.SignatureAlgorithm], claims)
	tokenString, err := token.SignedString([]byte(config.SecretKey))
	if err != nil {
		return "", err
	}
	return tokenString, nil
}

// GenerateJwtWithUserEntity GenerateJwt generate jwt token
func GenerateJwtWithUserEntity(user entities.UserEntity, config configs.JwtConfig) (string, error) {
	// tạo claims
	claims := jwt.MapClaims{
		"username": user.Username,
		"sub":      user.Username,
		"iat":      time.Now().Unix(),
		"exp":      time.Now().Add(time.Duration(config.AccessTokenExpiredMs * int64(time.Millisecond))).Unix(),
	}
	// tạo token với claims và sign token với secret key
	token := jwt.NewWithClaims(mapSignAlgorithm[config.SignatureAlgorithm], claims)
	tokenString, err := token.SignedString([]byte(config.SecretKey))
	if err != nil {
		return "", err
	}
	return tokenString, nil
}

// ParseJwt /*
func ParseJwt(jwtString string, config configs.JwtConfig) (jwt.MapClaims, error) {
	token, err := jwt.Parse(jwtString, func(token *jwt.Token) (interface{}, error) {
		if _, ok := token.Method.(*jwt.SigningMethodHMAC); !ok {
			return nil, &customerrors.UnauthorizedError{
				Message: fmt.Sprintf("unexpected signing method: %v", token.Header["alg"]),
				Code:    constants.ErrorCodeInvalidJwtToken,
			}
		}
		// return secret-key và err
		return []byte(config.SecretKey), nil
	})
	if err != nil {
		return nil, &customerrors.UnauthorizedError{
			Message: err.Error(),
			Code:    constants.ErrorCodeInvalidJwtToken,
		}
	}
	// lấy claims (type assertion)
	claims, ok := token.Claims.(jwt.MapClaims)
	if ok {
		return claims, nil
	} else {
		return nil, &customerrors.UnauthorizedError{
			Message: "extract claims error",
			Code:    constants.ErrorCodeInvalidJwtToken,
		}
	}
}

// ValidateJwt Hàm kiểm tra token có hợp lệ và còn thời hạn hay không
func ValidateJwt(jwtToken string) bool {
	mapClaims, parseJwtErr := ParseJwt(jwtToken, global.AppConfig.Jwt)
	if parseJwtErr != nil {
		return false
	}
	// Validate thời hạn token
	expireTime, err := mapClaims.GetExpirationTime()
	if err != nil || expireTime.Before(time.Now()) {
		return false
	}
	// trường hợp validate token hợp lệ
	return true
}

// GenerateRefreshToken Hàm generate refresh_token/**
func GenerateRefreshToken(user dtos.UserDetails, expiredAtInMs int64) string {
	return base64.StdEncoding.EncodeToString([]byte(fmt.Sprintf("%s%s$%d", user.Username, user.Email, expiredAtInMs)))
}
