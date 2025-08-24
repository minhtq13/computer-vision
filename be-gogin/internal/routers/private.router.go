package routers

import (
	"be-gogin/internal/databases"
	controllers3 "be-gogin/internal/domains/auth/controllers"
	"be-gogin/internal/domains/auth/dtos"
	controllers2 "be-gogin/internal/domains/messaging/controllers"
	controllers4 "be-gogin/internal/domains/notification/controllers"
	dtos2 "be-gogin/internal/domains/notification/dtos"
	"be-gogin/internal/domains/user/controllers"
	"be-gogin/internal/middlewares"

	"github.com/gin-gonic/gin"
)

// PrivateRouterGroup entering to route /private/users /**
type PrivateRouterGroup struct {
	authMiddleware           *middlewares.AuthenticationMiddleware
	databaseManager          *databases.DatabaseManager
	userController           *controllers.UserController
	messagingController      *controllers2.MessagingController
	authenticationController *controllers3.AuthenticationController
	notificationController   *controllers4.NotificationController
}

func NewPrivateRouterGroup(
	authMiddleware *middlewares.AuthenticationMiddleware,
	databaseManager *databases.DatabaseManager,
	userController *controllers.UserController,
	messagingController *controllers2.MessagingController,
	authenticationController *controllers3.AuthenticationController,
	notificationController *controllers4.NotificationController) *PrivateRouterGroup {
	return &PrivateRouterGroup{
		authMiddleware:           authMiddleware,
		databaseManager:          databaseManager,
		userController:           userController,
		messagingController:      messagingController,
		authenticationController: authenticationController,
		notificationController:   notificationController,
	}
}

func (pr *PrivateRouterGroup) InitPrivateRouter(routerGroup *gin.RouterGroup) {
	// routers cho api user
	userRouterPrivate := routerGroup.Group("/api/user").
		Use(pr.authMiddleware.Handle())
	{
		userRouterPrivate.GET("/profile", pr.userController.GetProfile)
	}

	// authentication
	authRouterPrivate := routerGroup.Group("/api/auth").
		Use(pr.authMiddleware.Handle())
	{
		authRouterPrivate.POST("/logout", middlewares.ValidateRequestBody[dtos.LogoutRequestDTO](),
			pr.authenticationController.Logout)
	}

	// notification
	notificationRouterPrivate := routerGroup.Group("/api/notifications").
		Use(pr.authMiddleware.Handle())
	{
		notificationRouterPrivate.POST("/fcm/send", middlewares.ValidateRequestBody[dtos2.NotificationFCMReqDTO](),
			pr.notificationController.SendFcmNotification)
	}

	// messaging
	messagingRouterPrivate := routerGroup.Group("/api/messaging").
		Use(pr.authMiddleware.Handle())
	{
		messagingRouterPrivate.POST("/kafka/send", pr.messagingController.ProduceKafkaMessage)
	}
}
