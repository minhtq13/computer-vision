package errors

import (
	"be-gogin/global"
	"be-gogin/global/constants"
	"be-gogin/pkg/utils"
	"errors"
	"github.com/gin-gonic/gin"
	"go.uber.org/zap"
	"net/http"
	"strings"
	"time"
)

type ErrorResponse struct {
	Code       string `json:"code,omitempty"`
	Status     int    `json:"status,omitempty"`
	Message    string `json:"message,omitempty"`
	Entity     string `json:"entity,omitempty"`
	FieldError string `json:"fieldError,omitempty"`
	Timestamp  string `json:"timestamp,omitempty"`
	Values     string `json:"values,omitempty"`
}

func ThrowInternalServerError(message string, path string) InternalServerError {
	return InternalServerError{
		Message:   message,
		Status:    http.StatusInternalServerError,
		Error:     constants.MessageInternalServerError,
		Timestamp: utils.ParseTimeToString(time.Now(), utils.YyyyMmDdHhMmSs),
		Path:      path,
	}
}

func ThrowResourceNotFoundError(resource, field string, values ...string) *BadRequestError {
	return &BadRequestError{
		Message:    constants.MessageResourceNotFound,
		Entity:     resource,
		Code:       constants.ErrorCodeResourceNotFound,
		FieldError: field,
		Values:     strings.Join(values, ", "),
	}
}

func ThrowBadRequestError(errorCode, resource, field string, values ...string) *BadRequestError {
	return &BadRequestError{
		Message:    constants.MapMessageError[errorCode],
		Entity:     resource,
		Code:       errorCode,
		FieldError: field,
		Values:     strings.Join(values, ", "),
	}
}

func LogError(err error, msg ...string) {
	global.Logger.Error(strings.Join(msg, ";"), zap.Error(err))
}

// HandleError xủ lý error -> trong request
func HandleError(context *gin.Context, err error) {
	var errorResponse ErrorResponse
	// khai báo các kiểu error cần catch và xử lý
	var badRequestError *BadRequestError
	var unauthorizedError *UnauthorizedError
	var accessDeniedError *AccessDeniedError
	var badCredentialError *BadCredentialError

	// kiểm tra loại error và xử lý tương ứng
	switch {
	case errors.As(err, &badRequestError):
		errorResponse = ErrorResponse{
			Message:    constants.MapMessageError[badRequestError.Code],
			Status:     http.StatusBadRequest,
			Entity:     badRequestError.Entity,
			Code:       badRequestError.Code,
			FieldError: badRequestError.FieldError,
			Values:     badRequestError.Values,
		}
	case errors.As(err, &badCredentialError):
		errorResponse = ErrorResponse{
			Message: badCredentialError.Error(),
			Status:  http.StatusUnauthorized,
		}
	case errors.As(err, &unauthorizedError):
		errorResponse = ErrorResponse{
			Message:   unauthorizedError.Error(),
			Status:    http.StatusUnauthorized,
			Timestamp: utils.ParseTimeToString(time.Now(), utils.YyyyMmDdHhMmSs),
			Code:      constants.ErrorCodeUnauthorized,
		}
	case errors.As(err, &accessDeniedError):
		errorResponse = ErrorResponse{
			Message:   accessDeniedError.Error(),
			Status:    http.StatusForbidden,
			Timestamp: utils.ParseTimeToString(time.Now(), utils.YyyyMmDdHhMmSs),
		}
	default:
		context.AbortWithStatusJSON(http.StatusInternalServerError, ThrowInternalServerError(err.Error(), context.FullPath()))
		return
	}
	context.AbortWithStatusJSON(errorResponse.Status, errorResponse)
}
