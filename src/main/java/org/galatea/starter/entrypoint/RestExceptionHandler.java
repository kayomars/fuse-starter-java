package org.galatea.starter.entrypoint;

import com.fasterxml.jackson.core.JsonProcessingException;
import javax.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

/**
 * A centralized REST handler that intercepts exceptions thrown by controller calls, enabling a
 * custom response to be returned.
 *
 * <p>The returned ResponseEntity body object will be serialised into JSON (hence the need for the
 * ApiError wrapper class).
 */
@ControllerAdvice
@Slf4j
public class RestExceptionHandler {

  // Failure in the data access API
  @ExceptionHandler(DataAccessException.class)
  protected ResponseEntity<Object> handleDataAccessException(final DataAccessException exception) {
    log.error("Unexpected data access error", exception);

    String errorMessage = "An internal application error occurred.";
    ApiError error = new ApiError(HttpStatus.INTERNAL_SERVER_ERROR, errorMessage);
    return buildResponseEntity(error);
  }

  // Reports the results of violations of constraints. More info would be great here.
  @ExceptionHandler(ConstraintViolationException.class)
  protected ResponseEntity<Object> handleConstraintViolation(
      final ConstraintViolationException exception) {
    log.debug("Invalid input data sent", exception);
    String errorMessage = ConstraintViolationMessageFormatter.toMessage(exception);

    ApiError error = new ApiError(HttpStatus.BAD_REQUEST, errorMessage);
    return buildResponseEntity(error);
  }

  // Reports problems encountered when parsing/generating JSON
  @ExceptionHandler(JsonProcessingException.class)
  protected ResponseEntity<Object> handleJsonProcessingException(
      final JsonProcessingException exception) {
    log.debug("Error converting to JSON", exception);
    ApiError error = new ApiError(HttpStatus.INTERNAL_SERVER_ERROR, exception.toString());
    return buildResponseEntity(error);
  }

  private ResponseEntity<Object> buildResponseEntity(final ApiError apiError) {
    return new ResponseEntity<>(apiError, apiError.getStatus());
  }

}
