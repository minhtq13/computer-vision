package com.elearning.elearning_support.security.models;

import com.elearning.elearning_support.entities.department.Department;
import com.elearning.elearning_support.entities.role.Role;
import com.elearning.elearning_support.entities.users.User;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

@Getter
@Setter
@NoArgsConstructor
public class CustomUserDetails implements UserDetails {

    @Setter
    private User user;

    @Setter
    private Set<String> roles;

    private Collection<? extends GrantedAuthority> authorities;

    private Set<Long> departmentIds;

    public CustomUserDetails(User user) {
        this.user = user;
        this.roles = user.getRoles().stream().map(Role::getCode).collect(Collectors.toSet());
        this.departmentIds = user.getDepartments().stream().map(Department::getId).collect(Collectors.toSet());

        List<GrantedAuthority> combinedAuthorities = new ArrayList<>();
        user.getRoles().forEach(role -> {
            // Add a role as an authority with "ROLE_" prefix
            combinedAuthorities.add(new SimpleGrantedAuthority(role.getCode()));
            // Add all permissions of that role
            role.getPermissions().forEach(permission -> {
                combinedAuthorities.add(new SimpleGrantedAuthority(permission.getCode()));
            });
        });
        this.authorities = combinedAuthorities;
    }

    public Long getUserId() {
        return this.user.getId();
    }

    @Override
    public String getPassword() {
        return user.getPassword();
    }

    @Override
    public String getUsername() {
        return user.getUsername();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
