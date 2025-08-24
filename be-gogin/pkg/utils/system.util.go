package utils

import (
	"be-gogin/global/constants"
	"os"
	"runtime"
)

// GetRootDirectory Hàm return đường dẫn của root directory của user /**
func GetRootDirectory() string {
	if userHomeDir, err := os.UserHomeDir(); err == nil {
		return userHomeDir
	}
	return "/"
}

// GetSharedDataDirectory Hàm return đường dẫn của folder chứa shared resources giữa các services theo loại OS tương ứng /**
func GetSharedDataDirectory() string {
	userHomeDir, err := os.UserHomeDir()
	if err == nil {
		if runtime.GOOS == "windows" {
			return userHomeDir + constants.WindowsSharedDataRelativeDir
		} else if runtime.GOOS == "linux" {
			return userHomeDir + constants.LinuxSharedDataRelativeDir
		}
	}
	return "/"
}
