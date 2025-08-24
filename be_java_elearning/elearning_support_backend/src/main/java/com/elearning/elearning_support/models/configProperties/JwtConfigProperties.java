package com.elearning.elearning_support.models.configProperties;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import lombok.Data;

@Data
@Component
@ConfigurationProperties(prefix = "jwt")
public class JwtConfigProperties {

    private String secretKey;

    private Long accessTokenExpiredMs;

    private Long refreshTokenExpiredMs;

    private String signatureAlgorithm;

}
