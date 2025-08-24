package com.elearning.elearning_support.utils.auth;

import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;
import org.springframework.security.core.context.SecurityContextHolder;
import com.elearning.elearning_support.constants.RoleConstants;
import com.elearning.elearning_support.constants.message.messageConst.MessageConst;
import com.elearning.elearning_support.entities.permission.Permission;
import com.elearning.elearning_support.entities.users.User;
import com.elearning.elearning_support.security.models.CustomUserDetails;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class AuthUtils {

    /**
     * Get current user id
     *
     * @return : current user id
     */
    public static Long getCurrentUserId() {
        try {
            User currentUser = getCurrentUser();
            return Objects.nonNull(currentUser) ? currentUser.getId() : null;
        } catch (Exception ex) {
            log.error(MessageConst.EXCEPTION_LOG_FORMAT, ex.getMessage(), ex);
            return null;
        }
    }

    /**
     * Get current user
     *
     * @return : current user
     */
    public static User getCurrentUser() {
        try {
            CustomUserDetails userDetails = (CustomUserDetails) SecurityContextHolder.getContext().getAuthentication()
                    .getPrincipal();
            return userDetails.getUser();
        } catch (Exception ex) {
            log.error(MessageConst.EXCEPTION_LOG_FORMAT, ex.getMessage(), ex);
            return null;
        }
    }

    /**
     * Get lst role code of the current user
     */
    public static Set<String> getCurrentUserRoles() {
        try {
            CustomUserDetails userDetails = (CustomUserDetails) SecurityContextHolder.getContext().getAuthentication()
                    .getPrincipal();
            return userDetails.getRoles();
        } catch (Exception ex) {
            log.error(MessageConst.EXCEPTION_LOG_FORMAT, ex.getMessage(), ex);
            return Collections.emptySet();
        }
    }

    /**
     * Get lst permission code of the current user
     */
    public static Set<String> getCurrentUserPermissions() {
        try {
            Set<String> permissions = new LinkedHashSet<>();
            CustomUserDetails userDetails = (CustomUserDetails) SecurityContextHolder.getContext().getAuthentication()
                    .getPrincipal();
            userDetails.getUser().getRoles().forEach(role -> {
                permissions.addAll(role.getPermissions().stream().map(Permission::getCode).collect(Collectors.toSet()));
            });
            return permissions;
        } catch (Exception ex) {
            log.error(MessageConst.EXCEPTION_LOG_FORMAT, ex.getMessage(), ex);
            return Collections.emptySet();
        }
    }

    /**
     * Get list department id of the current user
     */
    public static Set<Long> getCurrentUserDepartmentIds() {
        try {
            CustomUserDetails userDetails = (CustomUserDetails) SecurityContextHolder.getContext().getAuthentication()
                    .getPrincipal();
            return userDetails.getDepartmentIds();
        } catch (Exception ex) {
            log.error(MessageConst.EXCEPTION_LOG_FORMAT, ex.getMessage(), ex);
            return Collections.emptySet();
        }
    }

    /**
     * Get user's base role
     * 
     * @return
     */
    public static String getUserBasedRole() {
        Set<String> roles = getCurrentUserRoles();
        if (roles.isEmpty()) {
            return null;
        }
        if (roles.contains(RoleConstants.ROLE_ADMIN)) {
            return RoleConstants.ROLE_ADMIN;
        } else if (roles.contains(RoleConstants.ROLE_TEACHER)) {
            return RoleConstants.ROLE_TEACHER;
        } else {
            return RoleConstants.ROLE_STUDENT;
        }
    }

    /**
     * Check if the current user is an admin
     */
    public static Boolean isAdmin() {
        Set<String> roles = getCurrentUserRoles();
        if (roles.isEmpty())
            return Boolean.FALSE;
        return roles.containsAll(Arrays.asList(RoleConstants.ROLE_ADMIN, RoleConstants.ROLE_ADMIN_SYSTEM));
    }

    /**
     * Check if the current user is an admin of a department
     */
    public static Boolean isDepartmentAdmin() {
        Set<String> roles = getCurrentUserRoles();
        if (roles.isEmpty())
            return Boolean.FALSE;
        return roles.containsAll(Arrays.asList(RoleConstants.ROLE_ADMIN, RoleConstants.ROLE_ADMIN_DEPARTMENT));
    }

    /**
     * Check if the current user is an admin
     */
    public static Boolean isBaseTeacher() {
        Set<String> roles = getCurrentUserRoles();
        if (roles.isEmpty())
            return Boolean.FALSE;
        return roles.contains(RoleConstants.ROLE_TEACHER);
    }

    /**
     * Check if the current user is an admin
     */
    public static Boolean isBaseStudent() {
        Set<String> roles = getCurrentUserRoles();
        if (roles.isEmpty())
            return Boolean.FALSE;
        return roles.contains(RoleConstants.ROLE_STUDENT);
    }

}
