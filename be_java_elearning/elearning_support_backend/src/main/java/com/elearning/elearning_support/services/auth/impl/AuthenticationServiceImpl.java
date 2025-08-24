package com.elearning.elearning_support.services.auth.impl;

import java.util.Base64;
import java.util.Date;
import java.util.Objects;
import java.util.Set;
import javax.servlet.http.HttpServletRequest;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import com.elearning.elearning_support.constants.message.errorKey.ErrorKey;
import com.elearning.elearning_support.constants.message.messageConst.MessageConst;
import com.elearning.elearning_support.constants.message.messageConst.MessageConst.AuthInfo;
import com.elearning.elearning_support.dtos.auth.AuthValidationDTO;
import com.elearning.elearning_support.dtos.auth.login.LoginRequest;
import com.elearning.elearning_support.dtos.auth.login.LoginResponse;
import com.elearning.elearning_support.exceptions.CustomBadCredentialsException;
import com.elearning.elearning_support.security.models.CustomUserDetails;
import com.elearning.elearning_support.services.auth.AuthInfoService;
import com.elearning.elearning_support.services.auth.AuthenticationService;
import com.elearning.elearning_support.services.users.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class AuthenticationServiceImpl implements AuthenticationService {

    private final AuthenticationManager authenticationManager;

    private final AuthInfoService authInfoService;

    private final UserService userService;

    @Override
    public LoginResponse login(LoginRequest loginInfo, HttpServletRequest request) {
        // check exists by username
        if (!userService.existsByUsername(loginInfo.getUsername())) {
            throw new CustomBadCredentialsException(AuthInfo.USER_NAME_NOT_FOUND, MessageConst.UNAUTHORIZED,
                    ErrorKey.User.USERNAME, loginInfo.getUsername());
        }

        // decode and reverse
        String decodedPasswordStr = new String(Base64.getDecoder().decode(loginInfo.getPassword()));
        String rawPassword = new StringBuilder(decodedPasswordStr).reverse().toString();

        // do authentication
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                loginInfo.getUsername(), rawPassword);
        Authentication authentication = authenticationManager.authenticate(authenticationToken);
        SecurityContextHolder.getContext().setAuthentication(authentication);
        // get user details
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        if (Objects.isNull(userDetails)) {
            throw new CustomBadCredentialsException(MessageConst.AuthInfo.WRONG_USERNAME_PASSWORD,
                    MessageConst.UNAUTHORIZED,
                    String.format("%s/%s", ErrorKey.AuthInfo.USERNAME, ErrorKey.AuthInfo.PASSWORD));
        }
        // update auth info in db
        AuthValidationDTO authInfo = authInfoService.saveLoginAuthInfo(userDetails.getUser(), request);

        Set<String> roles = userDetails.getRoles();
        Set<String> permissions = userDetails.getAuthorities().stream()
                .map(a -> a.getAuthority())
                .filter(code -> !roles.contains(code)) // loại bỏ role code, chỉ lấy permission code
                .collect(Collectors.toSet());
        return LoginResponse.builder()
                .issuedAt(authInfo.getIssuedAt())
                .accessToken(authInfo.getAccessToken())
                .refreshToken(authInfo.getRefreshToken())
                .roles(roles)
                .permissions(permissions)
                .build();
    }
}
