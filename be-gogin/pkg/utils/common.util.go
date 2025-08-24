package utils

import (
	"be-gogin/global"
	"fmt"
	"runtime"
	"strings"
	"time"
)

func IIf[T any](cond bool, trueValue, falseValue T) T {
	if cond {
		return trueValue
	} else {
		return falseValue
	}
}

func CoalescePointer[T any](pointers ...*T) *T {
	if len(pointers) > 0 {
		for i := 0; i < len(pointers); i++ {
			if pointers[i] != nil {
				return pointers[i]
			}
		}
	}
	return nil
}

func FormatSqlQuery(rawSql string) string {
	return strings.ReplaceAll(rawSql, "${schema}", global.AppConfig.Databases.Postgres.Master.Schema+".")
}

func GetCallLocation() string {
	pc, _, line, _ := runtime.Caller(1)
	return fmt.Sprintf("%s:%d", runtime.FuncForPC(pc).Name(), line)
}

// LogFunction Hàm log function sử dùng closure -> state trạng thái/thời gian bắt đầu func được gọi
func LogFunction() func() {
	pc, _, _, _ := runtime.Caller(1)
	name := runtime.FuncForPC(pc).Name()
	now := time.Now()
	global.Logger.Sugar().Infof("=====ENTERED Function [%s] =====", name)
	return func() {
		global.Logger.Sugar().Infof("=====ENDED Function [%s] after [%v]ms =====", name, time.Since(now).Milliseconds())
	}
}
