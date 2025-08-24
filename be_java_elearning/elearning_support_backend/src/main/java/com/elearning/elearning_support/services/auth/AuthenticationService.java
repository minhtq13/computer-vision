package com.elearning.elearning_support.services.auth;

import javax.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Service;
import com.elearning.elearning_support.dtos.auth.login.LoginRequest;
import com.elearning.elearning_support.dtos.auth.login.LoginResponse;

@Service
public interface AuthenticationService {

    LoginResponse login(LoginRequest loginInfo, HttpServletRequest request);

}
