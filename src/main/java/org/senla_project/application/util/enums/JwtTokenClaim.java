package org.senla_project.application.util.enums;

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
