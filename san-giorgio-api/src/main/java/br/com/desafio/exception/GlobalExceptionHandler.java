package br.com.desafio.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

@ControllerAdvice
public class GlobalExceptionHandler {

    /**
     * Handles NotFoundAlertException and returns a response with HTTP status 404 (Not Found).
     * <p>
     * This method intercepts any {@link NotFoundAlertException} thrown within the application
     * and returns a standardized response with the status code 404 and the exception's message
     * as the response body.
     * </p>
     *
     * @param ex      the exception to handle
     * @param request the web request during which the exception occurred
     * @return a {@link ResponseEntity} with HTTP status 404 (Not Found) and the exception message as the body
     */
    @ExceptionHandler(NotFoundAlertException.class)
    public ResponseEntity<Object> handleNotFoundAlertException(NotFoundAlertException ex, WebRequest request) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
    }

    /**
     * Handles BadRequestAlertException and returns a response with HTTP status 400 (Bad Request).
     * <p>
     * This method intercepts any {@link BadRequestAlertException} thrown within the application
     * and returns a standardized response with the status code 400 and the exception's message
     * as the response body.
     * </p>
     *
     * @param ex      the exception to handle
     * @param request the web request during which the exception occurred
     * @return a {@link ResponseEntity} with HTTP status 400 (Bad Request) and the exception message as the body
     */
    @ExceptionHandler(BadRequestAlertException.class)
    public ResponseEntity<Object> handleBadRequestAlertException(BadRequestAlertException ex, WebRequest request) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
    }

}
