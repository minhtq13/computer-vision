package initializers

import (
	"be-gogin/global"
	"be-gogin/pkg/configs"
	"fmt"
	"log"
	"os"

	"github.com/spf13/viper"
)

func NewAppConfig() *configs.AppEnvConfig {
	configProfile := os.Getenv("ENV_MODE")
	if configProfile == "" {
		configProfile = "development"
	}
	loadedConfig := LoadEnvConfigObject(configProfile)
	global.AppConfig = loadedConfig
	return &loadedConfig
}

// LoadEnvConfigObject LoadEnvConfig load cấu hình theo môi trường dạng object -> tiện hơn /**
func LoadEnvConfigObject(env string) configs.AppEnvConfig {
	viperInst := viper.New()
	viperInst.AddConfigPath("./configs/")
	viperInst.SetConfigName(env)
	viperInst.SetConfigType("yml")

	// Đọc config
	err := viperInst.ReadInConfig()
	if err != nil {
		panic(fmt.Errorf("Failed to read config file: %s \n", err))
	}
	// read config to an object
	var appConfig configs.AppEnvConfig
	if readConfigErr := viperInst.Unmarshal(&appConfig); readConfigErr != nil {
		log.Fatal("Error mapping initializer environment config")
		return configs.AppEnvConfig{}
	}
	return appConfig
}

// LoadEnvConfigViper Load cấu hình dạng *Viper để phục vụ cho việc đọc config theo dạng path/**
func LoadEnvConfigViper(env string) *viper.Viper {
	viperInst := viper.New()
	viperInst.AddConfigPath("./configs/")
	viperInst.SetConfigName(env)
	viperInst.SetConfigType("yml")

	// Đọc config
	err := viperInst.ReadInConfig()
	if err != nil {
		panic(fmt.Errorf("Failed to read config file: %s \n", err))
	}
	return viperInst
}

func LoadConfig(env string) {
	LoadEnvConfigObject(env)
}
