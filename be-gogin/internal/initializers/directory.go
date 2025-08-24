package initializers

import (
	"be-gogin/internal/loggers"
	"be-gogin/pkg/utils"
	"fmt"
	"os"
	"path/filepath"
)

const DirectoriesInitLogTopic = "Directories-Initialization"

func InitDirectories() {
	// check and init the shared directory
	sharedDataDirPath := filepath.Clean(utils.GetSharedDataDirectory() + "/data")
	if _, fErr := os.Stat(sharedDataDirPath); fErr != nil {
		if os.IsNotExist(fErr) {
			loggers.Error(DirectoriesInitLogTopic, fErr, "The shared directory is not existing")
			mkDirErr := os.MkdirAll(sharedDataDirPath, os.ModePerm)
			if mkDirErr != nil {
				loggers.Error(DirectoriesInitLogTopic, mkDirErr, "Failed to create the shared directory")
			}
			loggers.Info(DirectoriesInitLogTopic, fmt.Sprintf("Created the shared directory at path: %s", sharedDataDirPath))
		} else {
			loggers.Error(DirectoriesInitLogTopic, fErr, "Failed to check the shared directory")
		}
	} else {
		loggers.Info(DirectoriesInitLogTopic, fmt.Sprintf("The shared directory is existing at path: %s", sharedDataDirPath))
	}
}
