package com.elearning.elearning_support.aop;

import java.util.Arrays;
import java.util.Set;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;
import com.elearning.elearning_support.security.constants.SecurityConstants;
import com.elearning.elearning_support.utils.auth.AuthUtils;
import lombok.extern.slf4j.Slf4j;

@Component("dynamicAccessAspect")
@Aspect
@Slf4j(topic = "DynamicAccessAspect")
public class DynamicAccessAspect {

    /**
     * Process API authorization checking per request
     * @param pjp processing join point
     * @throws Throwable e
     */
    @Around(value = "com.elearning.elearning_support.aop.AspectPointcut.springControllerPointcut()")
    public Object process(ProceedingJoinPoint pjp) throws Throwable {
        log.info("===== ENTERED DynamicAccessAspect =====");
        return pjp.proceed();
    }

    /**
     * Check if the current user has any required roles
     */
    public boolean hasAnyRole(String... roles) {
        Set<String> userRoles = AuthUtils.getCurrentUserRoles();
        return Arrays.stream(roles).anyMatch(roleName -> userRoles.contains(SecurityConstants.ROLE_PREFIX + roleName));
    }

    /**
     * Check if the current user has any required permission
     */
    public boolean hasAnyPermission(String... permissions) {
        Set<String> userRoles = AuthUtils.getCurrentUserPermissions();
        return Arrays.stream(permissions).anyMatch(userRoles::contains);
    }

}
