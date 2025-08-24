package com.elearning.elearning_support;

import java.io.File;
import java.util.Calendar;
import javax.annotation.PostConstruct;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import com.elearning.elearning_support.constants.SystemConstants;
import com.elearning.elearning_support.utils.file.FileUtils;
import lombok.extern.slf4j.Slf4j;

@EnableAsync
@SpringBootApplication
@EnableScheduling
@EnableConfigurationProperties
@EnableCaching
@Slf4j
public class ElearningSupportApplication {

    public static void main(String[] args) {
        SpringApplication.run(ElearningSupportApplication.class, args);
    }

    @PostConstruct
    public void init() {
        log.info("========= INITIALIZED TIMEZONE {} =========", Calendar.getInstance().getTimeZone().getID());

        // init shared app folder
        // data folder
        String sharedAppDirPath = FileUtils.getSharedAppDirectoryPath();
        File sharedDataFolder = new File(sharedAppDirPath + "/data");
        if (!sharedDataFolder.exists()) {
            boolean initSuccess = sharedDataFolder.mkdirs();
            log.info("========= INITIALIZED SHARED DATA FOLDER {} =========", initSuccess ? "SUCCESSFULLY" : "FAILED");
        }
        // logs folder
        File sharedLogsFolder = new File(SystemConstants.BASE_PATH + "/logs");
        if (!sharedLogsFolder.exists()) {
            boolean initSuccess = sharedLogsFolder.mkdirs();
            log.info("========= INITIALIZED LOG FOLDER {} =========", initSuccess ? "SUCCESSFULLY" : "FAILED");
        }
        log.info("========= INITIALIZED SHARED DATA, LOGS FOLDER =========");
    }

}
