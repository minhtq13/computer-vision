package com.elearning.elearning_support.configurations.firebase;

import java.io.File;
import java.io.FileInputStream;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.messaging.FirebaseMessaging;
import lombok.extern.slf4j.Slf4j;

@Configuration
@Slf4j
public class FirebaseConfig {

    @Value("${firebase.config}")
    private String firebaseConfigJson;

    @Value("${firebase.app-name}")
    private String appName;

    @Bean
    FirebaseMessaging firebaseMessaging() {
        try {
            File firebaseConfigResource = new File(firebaseConfigJson);
            log.info(" === LOG : { Firebase resource path : {} } ===", firebaseConfigResource.getPath());
            GoogleCredentials credentials = GoogleCredentials.fromStream(new FileInputStream(firebaseConfigResource));
            FirebaseOptions firebaseOptions = FirebaseOptions.builder().setCredentials(credentials).build();
            FirebaseApp app = FirebaseApp.initializeApp(firebaseOptions, appName);
            log.info(" === LOG : { Initialize FirebaseApp successfully! } ===");
            return FirebaseMessaging.getInstance(app);
        } catch (Exception exception) {
            log.error("==== ERROR : { Initialize FirebaseApp fail } ===");
            return null;
        }
    }

}
