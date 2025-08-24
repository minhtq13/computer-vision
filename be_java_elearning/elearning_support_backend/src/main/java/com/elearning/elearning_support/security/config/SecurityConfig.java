package com.elearning.elearning_support.security.config;

import java.util.Arrays;
import java.util.List;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.BeanIds;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.CorsUtils;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import com.elearning.elearning_support.models.configProperties.RouteAuthorizationConfigProperties;
import com.elearning.elearning_support.security.constants.SecurityConstants;
import com.elearning.elearning_support.security.constants.SecurityConstants.ApiWhiteListTypeEnum;
import com.elearning.elearning_support.security.exceptionHandlers.AccessDeniedHandler;
import com.elearning.elearning_support.security.exceptionHandlers.AuthEntryPoint;
import com.elearning.elearning_support.security.filters.ApiFilter;
import com.elearning.elearning_support.security.filters.JwtAuthenticationFilter;
import com.elearning.elearning_support.security.service.CustomUserDetailsService;
import lombok.RequiredArgsConstructor;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@RequiredArgsConstructor
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final CustomUserDetailsService userDetailsService;

    private final AccessDeniedHandler accessDeniedHandler;

    private final AuthEntryPoint authEntryPoint;

    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    private final ApiFilter apiFilter;

    private final RouteAuthorizationConfigProperties routeAuthorizationConfigProperties;

    @Value("${apis.whitelist:}")
    private String[] whiteListApis;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean(BeanIds.AUTHENTICATION_MANAGER)
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder());
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        String[] permitAllApis = routeAuthorizationConfigProperties.getApis().stream()
            .filter(api -> api.contains("|")
                && api.substring(api.lastIndexOf("|") + 1).equals(ApiWhiteListTypeEnum.NOT_REQUIRED_TOKEN.getType()))
            .map(api -> api.substring(0, api.lastIndexOf("|")))
            .toArray(String[]::new);
        http
            .csrf().disable()
            .cors()
            .and()
            .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            .and()
            .authorizeRequests()
            .requestMatchers(CorsUtils::isPreFlightRequest).permitAll()
            .antMatchers(routeAuthorizationConfigProperties.getPublicResources().toArray(new String[0])).permitAll()
            .antMatchers(permitAllApis).permitAll()
            .anyRequest().authenticated()
            .and()
            .exceptionHandling().authenticationEntryPoint(authEntryPoint)
            .and()
            .exceptionHandling().accessDeniedHandler(accessDeniedHandler);
        // customer filter chain
        http
            .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
            .addFilterBefore(apiFilter, JwtAuthenticationFilter.class);
    }

    /**
     * CORS Config
     */
    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(List.of("*"));
        configuration.setAllowedMethods(Arrays.asList(SecurityConstants.ALLOWED_METHODS));
        configuration.setAllowedHeaders(Arrays.asList(SecurityConstants.ALLOWED_HEADERS));
        configuration.setExposedHeaders(List.of("x-auth-token"));
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}

