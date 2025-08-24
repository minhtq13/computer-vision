package global

import (
	"be-gogin/internal/databases/tracers"
	"be-gogin/pkg/configs"
	"context"

	"go.uber.org/zap"
)

/*
Để lưu các biến/dependency sử dụng trong toàn bộ hệ thống
*/
var (
	// AppConfig Cấu hình ứng dụng
	AppConfig configs.AppEnvConfig
	// AuthorizedApiMap Map phân quyền api theo role-based
	AuthorizedApiMap   map[string][]string
	Logger             *zap.Logger
	PostgresSqlcTracer *tracers.SqlQueryTracer
)

// RootBackGroundContext context của server
var RootBackGroundContext = context.Background()
