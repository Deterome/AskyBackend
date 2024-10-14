package org.senla_project.application.util.exception;

public class SecurityTokenVerificationException extends RuntimeException {
  public SecurityTokenVerificationException(String message) {
    super(message);
  }
}
