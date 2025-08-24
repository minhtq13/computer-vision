package dtos

type NotificationFCMReqDTO struct {
	TargetUserId int64    `json:"targetUserId"`
	Title        string   `json:"title"`
	Content      string   `json:"content"`
	FcmTokens    []string `json:"fcmToken" binding:"required"`
}

type NotificationFcmResDTO struct {
	MessageIds   []string `json:"messageIds"`
	Success      bool     `json:"success"`
	Error        string   `json:"error,omitempty"`
	SuccessCount int      `json:"successCount"`
	FailedCount  int      `json:"failedCount"`
}
