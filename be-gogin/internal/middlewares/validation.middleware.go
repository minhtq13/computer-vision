package middlewares

import (
	"be-gogin/global/constants"
	internalerrors "be-gogin/internal/errors"
	"errors"
	"fmt"
	"net/http"
	"reflect"
	"strings"
	"time"

	"github.com/gin-gonic/gin"
	"github.com/go-playground/validator/v10"
)

func ValidateRequestBody[RequestBodyType any]() gin.HandlerFunc {
	return func(c *gin.Context) {
		var body RequestBodyType
		// Validate request body
		isPostOrPutRequest := c.Request.Method == http.MethodPost || c.Request.Method == http.MethodPut
		if bindErr := c.ShouldBindBodyWithJSON(&body); bindErr != nil && isPostOrPutRequest {
			var badRequestError internalerrors.BadRequestError
			badRequestError.Timestamp = time.Now()
			badRequestError.Entity = constants.ResourceRequestBody
			// Xử lý error, nếu error là dạng validation
			var fieldValidationErrors validator.ValidationErrors
			isValidationErr := errors.As(bindErr, &fieldValidationErrors)
			if isValidationErr {
				for _, fieldError := range fieldValidationErrors {
					processValidationError[RequestBodyType](body, fieldError, &badRequestError)
				}
			}
			c.AbortWithStatusJSON(http.StatusBadRequest, badRequestError)
			return
		}
		// forward to the next handler
		c.Set(constants.RequestBodyContextKey, body)
		c.Next()
	}
}

// ValidateRequestHeader Hàm validate request header/**
func ValidateRequestHeader[RequestHeaderType any]() gin.HandlerFunc {
	return func(c *gin.Context) {
		var header RequestHeaderType
		var badRequestError internalerrors.BadRequestError
		// Kiểm tra header validation errors
		if bindErr := c.ShouldBindHeader(&header); bindErr != nil {
			badRequestError.Timestamp = time.Now()
			badRequestError.Entity = constants.ResourceRequestsHeader
			// Kiểm tra và xử lý các validation error
			var validateErr validator.ValidationErrors
			isValidationErr := errors.As(bindErr, &validateErr)
			if isValidationErr {
				for _, fieldError := range validateErr {
					processValidationError[RequestHeaderType](header, fieldError, &badRequestError)
				}
			}
			c.AbortWithStatusJSON(http.StatusBadRequest, badRequestError)
			return
		}
		// forward to the next handler
		c.Set(constants.RequestHeaderContextKey, header)
		c.Next()
	}
}

// Hàm fill kiểm tra loại validation error và fill badRequestError response
func processValidationError[ObjectType any](object ObjectType, fieldError validator.FieldError, badRequestError *internalerrors.BadRequestError) {
	objectType := reflect.TypeOf(object)
	rfField, ok := objectType.FieldByName(fieldError.Field())
	// Lấy jsonFieldName của field để return response
	var jsonFieldName string
	if !ok {
		jsonFieldName = fieldError.Field()
	} else {
		// json:"fName,omitempty,...options" => fName
		jsonFieldName = strings.SplitN(rfField.Tag.Get("json"), ",", 2)[0]
		if jsonFieldName == "-" {
			jsonFieldName = ""
		}
	}
	switch fieldError.Tag() {
	case "required":
		badRequestError.FieldError = jsonFieldName
		badRequestError.Values = fieldError.Value().(string)
		badRequestError.Code = constants.ErrorCodeFieldMustNotEmpty
		badRequestError.Message = "Field " + jsonFieldName + " must not be empty"
	case "min":
		badRequestError.FieldError = jsonFieldName
		badRequestError.Values = fieldError.Value().(string)
		badRequestError.Code = constants.ErrorCodeFieldLengthMin
		badRequestError.Message = fmt.Sprintf("Field %s must contain at least %s characters", jsonFieldName, fieldError.Param())
	case "max":
		badRequestError.FieldError = jsonFieldName
		badRequestError.Values = fieldError.Value().(string)
		badRequestError.Code = constants.ErrorCodeFieldLengthMax
		badRequestError.Message = fmt.Sprintf("Field %s must contain at most %s characters", jsonFieldName, fieldError.Param())
	default:
		badRequestError.FieldError = jsonFieldName
		badRequestError.Values = fieldError.Value().(string)
		badRequestError.Code = constants.ErrorCodeInvalidFormat
		badRequestError.Message = "Invalid format"
	}
}
