package com.elearning.elearning_support.security.filters;

import java.io.IOException;
import java.util.Objects;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;
import com.elearning.elearning_support.constants.message.messageConst.MessageConst;
import com.elearning.elearning_support.dtos.auth.AuthValidationDTO;
import com.elearning.elearning_support.security.constants.SecurityConstants;
import com.elearning.elearning_support.security.jwt.JwtUtils;
import com.elearning.elearning_support.security.models.CustomUserDetails;
import com.elearning.elearning_support.security.service.CustomUserDetailsService;
import com.elearning.elearning_support.services.redis.RedisService;
import com.elearning.elearning_support.utils.object.ObjectMapperUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final CustomUserDetailsService userDetailsService;

    private final JwtUtils jwtUtils;

    private final RedisService redisService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
        throws ServletException, IOException {
        try {
            String token = jwtUtils.getTokenFromRequest(request);
            if (Objects.nonNull(token) && StringUtils.hasText(token) && jwtUtils.validateToken(token)) {
                String username = jwtUtils.getUsernameFromJwt(token);
                CustomUserDetails userDetails = (CustomUserDetails) userDetailsService.loadUserByUsername(username);
                AuthValidationDTO redisSessionData = ObjectMapperUtils.objectMapper(
                    redisService.getStringValue(SecurityConstants.AUTH_KEY_PREFIX + username), AuthValidationDTO.class);
                if (Objects.nonNull(userDetails) && Objects.nonNull(redisSessionData) && Objects.equals(redisSessionData.getAccessToken(), token)) {
                    UsernamePasswordAuthenticationToken tokenAuth = new UsernamePasswordAuthenticationToken(userDetails, null,
                        userDetails.getAuthorities());
                    tokenAuth.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(tokenAuth);
                }
            }
        } catch (Exception ex) {
            log.error("=====Token Authentication failed!=====");
            log.error(MessageConst.EXCEPTION_LOG_FORMAT, ex.getMessage(), ex.getCause(), ex);
        }
        filterChain.doFilter(request, response);
    }
}
