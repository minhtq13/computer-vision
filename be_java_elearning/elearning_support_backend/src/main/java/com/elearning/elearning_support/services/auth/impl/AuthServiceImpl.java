package com.elearning.elearning_support.services.auth.impl;

import static com.elearning.elearning_support.security.constants.SecurityConstants.AUTH_KEY_PREFIX;
import static com.elearning.elearning_support.security.constants.SecurityConstants.TOKEN_INVALID_STATUS;
import static com.elearning.elearning_support.security.constants.SecurityConstants.TOKEN_VALID_STATUS;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.Objects;
import java.util.concurrent.Executor;
import java.util.concurrent.TimeUnit;
import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import com.elearning.elearning_support.constants.CharacterConstants;
import com.elearning.elearning_support.constants.message.errorKey.ErrorKey;
import com.elearning.elearning_support.constants.message.messageConst.MessageConst;
import com.elearning.elearning_support.constants.message.messageConst.MessageConst.Resources;
import com.elearning.elearning_support.dtos.auth.AuthValidationDTO;
import com.elearning.elearning_support.dtos.auth.refresh.RefreshTokenResDTO;
import com.elearning.elearning_support.entities.auth.AuthInfo;
import com.elearning.elearning_support.entities.users.User;
import com.elearning.elearning_support.exceptions.CustomBadCredentialsException;
import com.elearning.elearning_support.exceptions.exceptionFactory.ExceptionFactory;
import com.elearning.elearning_support.models.configProperties.JwtConfigProperties;
import com.elearning.elearning_support.repositories.mongo.loginHistory.LoginHistoryMongoRepository;
import com.elearning.elearning_support.repositories.postgres.auth.AuthInfoRepository;
import com.elearning.elearning_support.security.jwt.JwtUtils;
import com.elearning.elearning_support.services.auth.AuthInfoService;
import com.elearning.elearning_support.services.redis.RedisService;
import com.elearning.elearning_support.utils.DateUtils;
import com.elearning.elearning_support.utils.http.HttpServletUtils;
import lombok.extern.slf4j.Slf4j;
import com.elearning.elearning_support.models.mongo.LoginHistoryDocument;
import com.elearning.elearning_support.utils.object.ObjectMapperUtils;

@Service
@Slf4j
public class AuthServiceImpl implements AuthInfoService {

    @Autowired
    private RedisService redisService;

    @Autowired
    private AuthInfoRepository authInfoRepository;

    @Autowired
    private ExceptionFactory exceptionFactory;

    @Autowired
    private LoginHistoryMongoRepository loginHistoryMongoRepository;

    @Autowired
    private JwtUtils jwtUtils;

    @Autowired
    private JwtConfigProperties jwtConfigProperties;

    @Autowired
    @Qualifier("commonAsyncTaskExecutor")
    private Executor commonExecutor;


    @Override
    public AuthValidationDTO saveLoginAuthInfo(User user, HttpServletRequest request) {
        // === CHECK TOKEN IN REDIS =====
        AuthValidationDTO redisAuth = null;
        String redisAccessToken = "";
        // check if redis connection/exception is corrupt
        try {
            if (redisService.isValidRedisTemplate()) {
                redisAuth = ObjectMapperUtils.objectMapper(redisService.getStringValue(AUTH_KEY_PREFIX + user.getUsername()),
                    AuthValidationDTO.class);
                redisAccessToken = Objects.nonNull(redisAuth) ? redisAuth.getAccessToken() : "";
                log.info("===== FOUND AUTHENTICATION DATA IN REDIS =====");
            }
        } catch (Exception exception) {
            log.error(MessageConst.EXCEPTION_LOG_FORMAT, exception.getMessage(), exception.getCause(), exception);
        }
        final String allIpAddresses = String.join(CharacterConstants.SEMICOLON, HttpServletUtils.getClientIPAddresses(request));
        if (Objects.isNull(redisAuth) || !jwtUtils.validateToken(redisAccessToken)) {
            AuthInfo currentAuthInfo = authInfoRepository.findFirstByUserIdOrderByCreatedAtDesc(user.getId()).orElse(null);
            String accessToken = jwtUtils.generateJwt(user);
            Date tokenIssuedAt = new Date();
            String refreshToken = jwtUtils.generateRefreshToken(user, System.currentTimeMillis() + jwtConfigProperties.getRefreshTokenExpiredMs());
            AuthValidationDTO authValidationDTO;
            if (Objects.isNull(currentAuthInfo)) { // not exist login history
                currentAuthInfo = AuthInfo.builder()
                    .userId(user.getId())
                    .token(accessToken)
                    .tokenIssuedAt(tokenIssuedAt)
                    .ipAddress(allIpAddresses)
                    .status(TOKEN_VALID_STATUS)
                    .createdAt(LocalDateTime.now())
                    .lastLoginAt(LocalDateTime.now())
                    .refreshToken(refreshToken)
                    .rfTokenExpiredAt(new Date(DateUtils.getCurrentDateTime().getTime() + jwtConfigProperties.getRefreshTokenExpiredMs()))
                    .build();
                authInfoRepository.save(currentAuthInfo);
            } else { // existed login history
                currentAuthInfo.setToken(accessToken); // set status valid
                currentAuthInfo.setTokenIssuedAt(tokenIssuedAt);
                currentAuthInfo.setRefreshToken(refreshToken);
                currentAuthInfo.setRfTokenExpiredAt(new Date(DateUtils.getCurrentDateTime().getTime() + jwtConfigProperties.getRefreshTokenExpiredMs()));
                currentAuthInfo.setStatus(TOKEN_VALID_STATUS);
                currentAuthInfo.setLastLoginAt(LocalDateTime.now());
                currentAuthInfo.setIpAddress(allIpAddresses);
                currentAuthInfo = authInfoRepository.save(currentAuthInfo);
            }
            // save login history
            saveLoginHistory(currentAuthInfo);
            authValidationDTO = new AuthValidationDTO(currentAuthInfo.getToken(), currentAuthInfo.getRefreshToken(), currentAuthInfo.getStatus(),
                currentAuthInfo.getTokenIssuedAt(), CharacterConstants.BLANK);
            // put to redis token store
            redisService.putStringWithExpiration(AUTH_KEY_PREFIX + user.getUsername(),
                ObjectMapperUtils.toJsonString(authValidationDTO), jwtConfigProperties.getAccessTokenExpiredMs() + 1000,
                TimeUnit.MILLISECONDS);
            return authValidationDTO;
        } else {
            return redisAuth;
        }
    }

    @Override
    public AuthInfo findByUserId(Long userId) {
        return authInfoRepository.findFirstByUserIdOrderByCreatedAtDesc(userId).orElse(null);
    }

    @Override
    public RefreshTokenResDTO refreshAccessToken(String refreshToken) {
        AuthInfo authInfo = authInfoRepository.findByRefreshToken(refreshToken)
            .orElseThrow(() -> exceptionFactory.resourceNotFoundException(MessageConst.AuthInfo.NOT_FOUND, Resources.AUTH_INFORMATION,
                MessageConst.RESOURCE_NOT_FOUND, ErrorKey.AuthInfo.REFRESH_TOKEN));
        if (!jwtUtils.validateToken(authInfo.getToken()) || Objects.equals(authInfo.getStatus(), TOKEN_INVALID_STATUS)) {
            User user = authInfo.getUser();
            authInfo.setToken(jwtUtils.generateJwt(user));
            authInfo.setStatus(TOKEN_VALID_STATUS);
            authInfo.setTokenIssuedAt(new Date());
            // check refresh token if expired -> gen new refresh token
            if (authInfo.getRfTokenExpiredAt().before(DateUtils.getCurrentDateTime())) {
                throw new CustomBadCredentialsException(MessageConst.AuthInfo.REFRESH_TOKEN_EXPIRED, Resources.AUTH_INFORMATION,
                    ErrorKey.AuthInfo.REFRESH_TOKEN);
            }
            authInfo = authInfoRepository.save(authInfo);
            redisService.putStringWithExpiration(AUTH_KEY_PREFIX + user.getUsername(),
                ObjectMapperUtils.toJsonString(
                    new AuthValidationDTO(authInfo.getToken(), authInfo.getRefreshToken(), authInfo.getStatus(), authInfo.getTokenIssuedAt(),
                        CharacterConstants.BLANK)), jwtConfigProperties.getAccessTokenExpiredMs() + 1000, TimeUnit.MILLISECONDS);
        }
        return new RefreshTokenResDTO(authInfo.getToken(), authInfo.getRefreshToken());
    }

    /**
     * Save login history information to mongodb
     */
    private void saveLoginHistory(AuthInfo authInfo) {
        commonExecutor.execute(() -> {
            LoginHistoryDocument loginHistory = new LoginHistoryDocument(authInfo);
            loginHistoryMongoRepository.save(loginHistory);
        });
    }
}
