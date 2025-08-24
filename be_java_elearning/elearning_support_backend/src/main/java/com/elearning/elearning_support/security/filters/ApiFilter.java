package com.elearning.elearning_support.security.filters;

import java.io.IOException;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import com.elearning.elearning_support.constants.CharacterConstants;
import com.elearning.elearning_support.utils.http.HttpServletUtils;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class ApiFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
        throws ServletException, IOException {
        log.info("Request {} {} to \"{}\" from the Client-IP: {}", request.getScheme().toUpperCase(), request.getMethod(), request.getRequestURI(), String.join(
            CharacterConstants.SEMICOLON, HttpServletUtils.getClientIPAddresses(request)));
        filterChain.doFilter(request, response);
    }
}
