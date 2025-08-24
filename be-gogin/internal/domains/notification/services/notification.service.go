package services

import (
	"be-gogin/internal/domains/notification/dtos"
	"context"
)

type INotificationService interface {
	SendFcmNotification(ctx context.Context, messageReq dtos.NotificationFCMReqDTO) (dtos.NotificationFcmResDTO, error)
	SendFcmMulticastNotification(ctx context.Context, messageReq dtos.NotificationFCMReqDTO) (dtos.NotificationFcmResDTO, error)
}
