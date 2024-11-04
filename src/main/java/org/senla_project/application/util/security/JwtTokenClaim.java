package org.senla_project.application.util.security;

public enum JwtTokenClaim {

    ROLES("roles");

    final String claimName;

    JwtTokenClaim(String claimName) {
        this.claimName = claimName;
    }

    @Override
    public String toString() {
        return this.claimName;
    }

}
