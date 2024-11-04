package org.senla_project.application.util.security;

import lombok.experimental.UtilityClass;
import org.senla_project.application.util.data.DefaultRole;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.List;

@UtilityClass
public class AuthenticationManager {

    public boolean ifUsernameBelongsToAuthenticatedUser(String username) {
        return getUsernameOfAuthenticatedUser().equals(username);
    }

    public boolean isAuthenticatedUserAnAdmin() {
        return getRoleStringsOfAuthenticatedUser()
                .contains(DefaultRole.ADMIN.toString());
    }

    public String getUsernameOfAuthenticatedUser() {
        return getCurrentAuthentication()
                .getName();
    }

    public List<String> getRoleStringsOfAuthenticatedUser() {
        return getCurrentAuthentication()
                .getAuthorities()
                .stream()
                .map(GrantedAuthority::getAuthority)
                .toList();
    }

    public Authentication getCurrentAuthentication() {
        return SecurityContextHolder
                .getContext()
                .getAuthentication();
    }

}
