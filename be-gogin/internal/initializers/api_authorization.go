package initializers

import (
	"be-gogin/global"
	"be-gogin/global/constants"
	"be-gogin/internal/loggers"
	"be-gogin/pkg/configs"
	"fmt"
	"strings"

	"github.com/samber/lo"
	"github.com/spf13/viper"
)

const ApiAuthorizationLogTopic = "Api-Authorization-Config"

func InitApiAuthorizationConfig() {
	apiAuthConfig, err := LoadApiAuthorizationConfig()
	if err != nil {
		loggers.Panic(ApiAuthorizationLogTopic, err, "Error loading api authorization config")
	}

	// convert sang dạng map để tiện cho việc check
	authorizedApiMap := make(map[string][]string)
	for _, api := range apiAuthConfig.Apis {
		subApiPath := strings.Replace(api, constants.ServerDefaultPrefix, "", 1)[:strings.LastIndex(api, "|")]
		roleStrings := strings.Split(api[strings.LastIndex(api, "|")+1:], ",")
		roleSlice := make([]string, 0, 4)
		lo.ForEach(roleStrings, func(item string, index int) {
			roleSlice = append(roleSlice, constants.ApiRoleBaseAuthorizationMap[item])
		})
		authorizedApiMap[subApiPath] = roleSlice
	}
	// set map phân quyền vào biến global
	global.AuthorizedApiMap = authorizedApiMap
}

// LoadApiAuthorizationConfig Load cấu hình phân quyền API theo role /**
func LoadApiAuthorizationConfig() (configs.ApiAuthConfig, error) {
	viperInst := viper.New()
	viperInst.AddConfigPath("./configs/authorization")
	viperInst.SetConfigName("authorization")
	viperInst.SetConfigType("yml")

	// Đọc config
	err := viperInst.ReadInConfig()
	if err != nil {
		loggers.Panic(ApiAuthorizationLogTopic, err, fmt.Sprintf("Failed to read config file"))
		panic(fmt.Errorf("Failed to read config file: %s \n", err))
	}
	// read config to an object
	var apiAuthConfig configs.ApiAuthConfig
	readConfigErr := viperInst.Unmarshal(&apiAuthConfig)
	if readConfigErr != nil {
		loggers.Panic(ApiAuthorizationLogTopic, readConfigErr, "Error loading api authorization config")
		return configs.ApiAuthConfig{}, readConfigErr
	}
	return apiAuthConfig, nil
}
