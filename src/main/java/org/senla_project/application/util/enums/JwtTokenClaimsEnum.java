package org.senla_project.application.util.enums;

public enum JwtTokenClaimsEnum {

    ROLES("roles");

    final String claimName;

    JwtTokenClaimsEnum(String claimName) {
        this.claimName = claimName;
    }

    @Override
    public String toString() {
        return this.claimName;
    }

}
