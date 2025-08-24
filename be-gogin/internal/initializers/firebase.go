package initializers

import (
	"be-gogin/global"
	"be-gogin/internal/loggers"
	"be-gogin/pkg/configs"

	firebase "firebase.google.com/go/v4"
	"firebase.google.com/go/v4/messaging"
	"google.golang.org/api/option"
)

const FirebaseInitLogTopic = "Firebase"

type Firebase struct {
	application     *firebase.App
	messagingClient *messaging.Client
}

func (f *Firebase) GetMessagingClient() *messaging.Client {
	return f.messagingClient
}

func NewFirebase(appConfig *configs.AppEnvConfig) *Firebase {
	firebaseConfig := appConfig.Firebase
	opt := option.WithCredentialsFile("./" + firebaseConfig.KeyFile)
	config := &firebase.Config{
		ProjectID: firebaseConfig.ProjectID,
	}

	// Khởi tạo firebase application
	application, appInitErr := firebase.NewApp(global.RootBackGroundContext, config, opt)
	if appInitErr != nil {
		loggers.Error(FirebaseInitLogTopic, appInitErr, "Failed to initialize firebase application")
		return nil
	}
	loggers.Info(FirebaseInitLogTopic, "Initialized firebase application")

	// Khởi tạo messagingClient client
	messagingClient, mcInitErr := application.Messaging(global.RootBackGroundContext)
	if mcInitErr != nil {
		loggers.Error(FirebaseInitLogTopic, mcInitErr, "Failed to initialize firebase messaging client")
		return nil
	}
	loggers.Info(FirebaseInitLogTopic, "Initialized firebase messaging client")

	return &Firebase{
		application:     application,
		messagingClient: messagingClient,
	}
}
