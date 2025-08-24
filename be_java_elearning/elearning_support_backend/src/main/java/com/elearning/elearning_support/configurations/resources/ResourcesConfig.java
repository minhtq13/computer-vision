package com.elearning.elearning_support.configurations.resources;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import com.elearning.elearning_support.constants.SystemConstants;

@Configuration
public class ResourcesConfig implements WebMvcConfigurer {

    private static final String[] CLASSPATH_RESOURCE_LOCATIONS =
        {
            "classpath:/META-INF/resources/",
            "classpath:/resources/",
            "classpath:/static/",
            "classpath:/public/",
            "classpath:/custom/"
        };

    @Value("${app.file-storage.resource-path}")
    private String filePathResource;

    private final String sharedDirectory = SystemConstants.IS_WINDOWS ? SystemConstants.WINDOWS_SHARED_DIR : SystemConstants.LINUX_SHARED_DIR;

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // config resourceHandlers and configure resourceLocation
        // fileResource : fileLocation + /**
        // fileLocation : ../resources/upload/files/
        registry.addResourceHandler(filePathResource, "/public/shared/**")
            .addResourceLocations(String.format("file:///%s/", SystemConstants.RESOURCE_PATH), String.format("file:///%s/", sharedDirectory));
    }
}
