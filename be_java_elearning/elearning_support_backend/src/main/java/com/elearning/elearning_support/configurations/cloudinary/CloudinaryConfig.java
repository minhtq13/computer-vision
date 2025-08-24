package com.elearning.elearning_support.configurations.cloudinary;

import java.util.HashMap;
import java.util.Map;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import com.cloudinary.Cloudinary;

@Configuration
public class CloudinaryConfig {


    @Value("${cloudinary.cloud.name}")
    private String cloudName;

    @Value("${cloudinary.cloud.api-key}")
    private String apiKey;

    @Value("${cloudinary.cloud.api-secret}")
    private String apiSecret;


    @Bean
    public Cloudinary cloudinary(){
        Map<String, String> config = new HashMap();
        config.put("cloud_name", cloudName);
        config.put("api_key", apiKey);
        config.put("api_secret", apiSecret);
        return new Cloudinary(config);
    }
}
