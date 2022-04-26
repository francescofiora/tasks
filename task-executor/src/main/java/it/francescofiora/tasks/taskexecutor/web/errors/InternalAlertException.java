package it.francescofiora.tasks.taskexecutor.web.errors;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

/**
 * Internal Alert Exception.
 */
public class InternalAlertException extends ResponseStatusException {

  private static final long serialVersionUID = 1L;

  public InternalAlertException(String reason, Throwable cause) {
    super(HttpStatus.INTERNAL_SERVER_ERROR, reason, cause);
  }
}
