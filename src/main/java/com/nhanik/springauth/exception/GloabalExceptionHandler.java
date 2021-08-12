package com.nhanik.springauth.exception;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.time.LocalDateTime;

@ControllerAdvice
public class GloabalExceptionHandler extends ResponseEntityExceptionHandler {

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(
            MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        ValidationExceptionResponse response = new ValidationExceptionResponse(
                LocalDateTime.now(), status.value(), "Validation failure"
        );
        ex.getBindingResult()
                .getFieldErrors()
                .forEach(error -> response.addValidationError(error.getField(), error.getDefaultMessage()));
        return new ResponseEntity<>(response, headers, status);
    }

    @ExceptionHandler({
            MalformedEmailException.class,
            RegistrationFailureException.class})
    public ResponseEntity<ExceptionResponse> handleBadRequest(Exception ex) {
        ExceptionResponse response = new ExceptionResponse(
                LocalDateTime.now(), HttpStatus.BAD_REQUEST.value(), ex.getMessage()
        );
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ExceptionResponse> handleNotFound(Exception ex) {
        ExceptionResponse response = new ExceptionResponse(
                LocalDateTime.now(), HttpStatus.NOT_FOUND.value(), ex.getMessage()
        );
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }
}
