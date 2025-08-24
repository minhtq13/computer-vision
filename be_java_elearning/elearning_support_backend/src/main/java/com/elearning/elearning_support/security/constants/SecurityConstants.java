package com.elearning.elearning_support.security.constants;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;


@Data
public class SecurityConstants {

    public static final String BEARER_AUTH_SCHEME = "Bearer";

    public static final String API_KEY = "ELEARNING_SUPPORT";

    public static final String AUTH_HEADER = "Authorization";

    public static final String[] ALLOWED_HEADERS = {
        "authorization",
        "content-type",
        "x-auth-token",
        "refreshToken"
    };

    public static final String[] ALLOWED_METHODS = {
        "GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS"
    };

    public static final String AUTH_KEY_PREFIX = "ELS@CACHE-AUTH_USERNAME_";

    public static final Integer TOKEN_INVALID_STATUS = 0;

    public static final Integer TOKEN_VALID_STATUS = 1;

    public static final String ROLE_PREFIX = "ROLE_";

    @Getter
    @AllArgsConstructor
    public enum ApiWhiteListTypeEnum {
        NOT_REQUIRED_TOKEN("-1"),
        REQUIRED_TOKEN("0"),
        SUPER_ADMIN("1"),
        TEACHER("2"),
        STUDENT("3");
        private final String type;
    }

}
