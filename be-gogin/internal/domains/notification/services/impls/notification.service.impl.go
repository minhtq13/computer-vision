package impls

import (
	"be-gogin/internal/domains/notification/dtos"
	"be-gogin/internal/domains/notification/services"
	"be-gogin/internal/errors"
	"be-gogin/internal/initializers"
	"context"
	"firebase.google.com/go/v4/messaging"
	"github.com/samber/lo"
	"strconv"
)

type NotificationService struct {
	firebase *initializers.Firebase
}

func NewNotificationService(firebase *initializers.Firebase) services.INotificationService {
	return &NotificationService{
		firebase: firebase,
	}
}

// SendFcmNotification Hàm send fcm message/*
func (notificationService *NotificationService) SendFcmNotification(ctx context.Context, messageReq dtos.NotificationFCMReqDTO) (dtos.NotificationFcmResDTO, error) {
	// tạo single message
	data := map[string]string{
		"title":  messageReq.Title,
		"body":   messageReq.Content,
		"target": "TU" + strconv.FormatInt(messageReq.TargetUserId, 10),
	}
	message := &messaging.Message{
		Notification: &messaging.Notification{
			Title: messageReq.Title,
			Body:  messageReq.Content,
		},
		Data:  data,
		Token: messageReq.FcmTokens[0],
	}

	messageId, sendErr := notificationService.firebase.GetMessagingClient().Send(ctx, message)
	if sendErr != nil {
		errors.LogError(sendErr)
		return dtos.NotificationFcmResDTO{
			Success:      false,
			Error:        sendErr.Error(),
			MessageIds:   []string{messageId},
			FailedCount:  1,
			SuccessCount: 0,
		}, sendErr
	}
	return dtos.NotificationFcmResDTO{
		Success:      true,
		MessageIds:   []string{messageId},
		FailedCount:  0,
		SuccessCount: 1,
	}, nil
}

// SendFcmMulticastNotification Hàm send multicast fcm message/*
func (notificationService *NotificationService) SendFcmMulticastNotification(ctx context.Context, messageReq dtos.NotificationFCMReqDTO) (dtos.NotificationFcmResDTO, error) {
	// tạo multicast message
	data := map[string]string{
		"title":  messageReq.Title,
		"body":   messageReq.Content,
		"target": "TU" + strconv.FormatInt(messageReq.TargetUserId, 10),
	}
	message := &messaging.MulticastMessage{
		Notification: &messaging.Notification{
			Title: messageReq.Title,
			Body:  messageReq.Content,
		},
		Data:   data,
		Tokens: messageReq.FcmTokens,
	}

	batchResponse, sendErr := notificationService.firebase.GetMessagingClient().SendEachForMulticast(ctx, message)
	// any error
	if sendErr != nil {
		errors.LogError(sendErr)
		return dtos.NotificationFcmResDTO{
			Error: sendErr.Error(),
			MessageIds: lo.Map(batchResponse.Responses, func(item *messaging.SendResponse, index int) string {
				return item.MessageID
			}),
			SuccessCount: batchResponse.SuccessCount,
			FailedCount:  batchResponse.FailureCount,
		}, sendErr
	}
	// success toàn bộ
	return dtos.NotificationFcmResDTO{
		MessageIds: lo.Map(batchResponse.Responses, func(item *messaging.SendResponse, index int) string {
			return item.MessageID
		}),
		SuccessCount: batchResponse.SuccessCount,
		FailedCount:  batchResponse.FailureCount,
	}, nil
}
