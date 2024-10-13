package org.senla_project.application.util.exception;

public class SecurityTokenExpiredException extends RuntimeException {
    public SecurityTokenExpiredException(String message) {
        super(message);
    }
}
