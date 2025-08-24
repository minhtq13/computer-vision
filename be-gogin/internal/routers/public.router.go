package routers

import (
	"be-gogin/internal/databases"
	"be-gogin/internal/domains/auth/controllers"
	"be-gogin/internal/domains/auth/dtos"
	"be-gogin/internal/middlewares"

	"github.com/gin-gonic/gin"
)

type PublicRouterGroup struct {
	databaseManager          *databases.DatabaseManager
	authenticationController *controllers.AuthenticationController
}

func NewPublicRouterGroup(
	databaseManager *databases.DatabaseManager,
	authenticationController *controllers.AuthenticationController) *PublicRouterGroup {
	return &PublicRouterGroup{
		databaseManager:          databaseManager,
		authenticationController: authenticationController,
	}
}

func (pr *PublicRouterGroup) InitPublicRouter(routerGroup *gin.RouterGroup) {
	// routers cho auth apis
	authRouterGroup := routerGroup.Group("/api/auth")
	{
		authRouterGroup.
			POST("/login",
				middlewares.ValidateRequestBody[dtos.LoginRequestDTO](),
				middlewares.Transactional(pr.databaseManager),
				pr.authenticationController.Login).
			POST("/token/refresh",
				middlewares.ValidateRequestHeader[dtos.RefreshTokenRequestHeader](),
				pr.authenticationController.RefreshToken)
	}
}
