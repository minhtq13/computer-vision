package middlewares

import (
	"be-gogin/global"
	"be-gogin/global/constants"
	authservices "be-gogin/internal/domains/auth/services"
	"be-gogin/internal/domains/common/utils"
	"be-gogin/internal/errors"
	"net/http"
	"slices"
	"strings"

	"github.com/gin-gonic/gin"
)

type AuthenticationMiddleware struct {
	authService authservices.IAuthenticationService
}

func NewAuthenticationMiddleware(authService authservices.IAuthenticationService) *AuthenticationMiddleware {
	return &AuthenticationMiddleware{
		authService: authService,
	}
}

/**
Handler implementation
*/

func (authMiddleware *AuthenticationMiddleware) Handle() gin.HandlerFunc {
	return func(context *gin.Context) {
		tokenHeader := context.GetHeader(constants.AuthorizationHeader)
		// verify token
		var tokenValid bool
		var tokenValidateErr error
		// kiểm tra tính xác thực của token (user + phân quyền)
		tokenValid, tokenValidateErr = authMiddleware.authService.VerifyToken(tokenHeader, context)

		// Phân quyền API theo Role-Based
		apiPath := strings.Replace(context.FullPath(), constants.ServerDefaultPrefix, "", 1)
		authorizedRoles := global.AuthorizedApiMap[apiPath]
		currentUserRoles := utils.GetCurrentUserRoles(context)
		accessDeniedErr := &errors.AccessDeniedError{
			Status:  http.StatusForbidden,
			Message: constants.MessageAccessDenied,
		}
		// Kiểm tra phân quyền api có cần token hay không
		if slices.Contains(authorizedRoles, constants.NoRequiredToken) { // không yêu cầu token đăng nhập
			context.Next()
			return
		} else if slices.Contains(authorizedRoles, constants.RequiredToken) { // yêu cầu token và allow mọi role
			if tokenHeader == "" {
				errors.HandleError(context, accessDeniedErr)
				return
			} else if !tokenValid {
				errors.HandleError(context, tokenValidateErr)
				return
			} else {
				context.Next()
				return
			}
		} else if slices.Contains(authorizedRoles, constants.RoleSuperAdmin) { // Chỉ dành cho admin
			if slices.Contains(currentUserRoles, constants.RoleSuperAdminCode) {
				context.Next()
				return
			} else {
				errors.HandleError(context, accessDeniedErr)
			}
		} else if slices.Contains(authorizedRoles, constants.RoleTeacher) { // Chỉ dành cho giảng viên
			if slices.Contains(currentUserRoles, constants.RoleTeacherCode) {
				context.Next()
				return
			} else {
				errors.HandleError(context, accessDeniedErr)
				return
			}
		} else if slices.Contains(authorizedRoles, constants.RoleStudent) { // Chỉ dành cho sinh viên
			if slices.Contains(currentUserRoles, constants.RoleStudentCode) {
				context.Next()
				return
			} else {
				errors.HandleError(context, accessDeniedErr)
				return
			}
		} else { // invalid authorization
			errors.HandleError(context, accessDeniedErr)
			return
		}
	}
}
