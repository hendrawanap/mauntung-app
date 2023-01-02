package com.mauntung.mauntung.adapter.http.exception;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.validation.ConstraintViolationException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@ControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {
    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        Map<String, List<String>> body = new HashMap<>();

        List<String> errors = ex.getBindingResult()
            .getFieldErrors()
            .stream()
            .map(this::buildErrorMessageFromFieldError)
            .collect(Collectors.toList());

        body.put("errors", errors);

        return handleExceptionInternal(ex, body, headers, HttpStatus.BAD_REQUEST, request);
    }

    @ExceptionHandler(value = { IllegalArgumentException.class, IllegalStateException.class, ConstraintViolationException.class })
    protected ResponseEntity<Object> handleIllegalArgument(RuntimeException ex, WebRequest request) {
        Map<String, List<String>> body = new HashMap<>();
        body.put("errors", List.of(ex.getMessage()));

        if (ex instanceof ConstraintViolationException) {
            body.put("errors", buildErrorMessagesFromConstraintViolationExceptionMessage(ex.getMessage()));
        }

        return handleExceptionInternal(ex, body, new HttpHeaders(), HttpStatus.BAD_REQUEST, request);
    }

    private String buildErrorMessageFromFieldError(FieldError error) {
        return String.format("%s %s", error.getField(), error.getDefaultMessage());
    }

    private List<String> buildErrorMessagesFromConstraintViolationExceptionMessage(String message) {
        return List.of(message.split(", "));
    }
}
