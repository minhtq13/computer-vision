package com.elearning.elearning_support.models.configProperties;

import java.util.List;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import lombok.Data;

@Component
@ConfigurationProperties(prefix = "routes")
@Data
public class RouteAuthorizationConfigProperties {
    private List<String> apis;
    private List<String> publicResources;
}
