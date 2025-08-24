package controllers

import (
	"be-gogin/global/constants"
	"be-gogin/internal/domains/notification/dtos"
	"be-gogin/internal/domains/notification/services"
	"be-gogin/internal/errors"
	"github.com/gin-gonic/gin"
	"net/http"
)

type NotificationController struct {
	notificationService services.INotificationService
}

func NewNotificationController(notificationService services.INotificationService) *NotificationController {
	return &NotificationController{
		notificationService: notificationService,
	}
}

// SendFcmNotification Controller test gủi thông báo qua FCM
func (notificationController *NotificationController) SendFcmNotification(c *gin.Context) {
	messageReq := c.MustGet(constants.RequestBodyContextKey).(dtos.NotificationFCMReqDTO)
	res, err := notificationController.notificationService.SendFcmNotification(c, messageReq)
	if err != nil {
		errors.HandleError(c, err)
		return
	}
	c.JSON(http.StatusOK, res)
}
