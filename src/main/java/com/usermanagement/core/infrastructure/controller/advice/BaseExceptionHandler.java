package com.usermanagement.core.infrastructure.controller.advice;

import com.usermanagement.core.application.config.exceptions.BaseException;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;

@RestControllerAdvice
public class BaseExceptionHandler {

    /**
     * Handles exceptions of type BaseException thrown within the application.
     * Constructs a response entity containing the error message and sets the HTTP status to BAD REQUEST.
     *
     * @param ex the caught BaseException instance.
     * @return a ResponseEntity object containing a ResponseError with the exception's message and a BAD REQUEST status code.
     */
    @ExceptionHandler(BaseException.class)
    public ResponseEntity<ResponseError> handleCustomException(BaseException ex) {
        return buildErrorResponse(ex.getMessage(), HttpStatus.BAD_REQUEST.value());
    }

    /**
     * Handles MethodArgumentNotValidException exceptions, typically thrown when @Validated
     * validation fails on a controller method parameter. Aggregates all field error messages
     * into a single error message.
     *
     * @param ex the caught MethodArgumentNotValidException instance.
     * @return a ResponseEntity object containing a ResponseError with the aggregated error messages
     * and a BAD REQUEST status code.
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ResponseError> handleValidationExceptions(MethodArgumentNotValidException ex) {
        var errors = ex.getBindingResult().getFieldErrors().stream()
                .map(DefaultMessageSourceResolvable::getDefaultMessage)
                .toList();

        var errorMessage = String.join(", ", errors);
        return buildErrorResponse(errorMessage, HttpStatus.BAD_REQUEST.value());
    }

    /**
     * Builds a ResponseEntity with a ResponseError payload. This method creates a standardized error response
     * structure containing the current time, error message, HTTP status, and a custom error code.
     *
     * @param message the error message to be included in the response.
     * @param code    the custom error code representing the specific error condition.
     * @return a ResponseEntity containing the constructed ResponseError object.
     */
    private ResponseEntity<ResponseError> buildErrorResponse(String message, int code) {
        return ResponseEntity.ok((new ResponseError(
                LocalDateTime.now(), message, HttpStatus.BAD_REQUEST, code
        )));
    }

}