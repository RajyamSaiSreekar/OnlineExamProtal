package com.OnlineExamProtal.UserModule.Exception;

import jakarta.validation.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(RuntimeException.class)
    ResponseEntity RuntimeException(RuntimeException ex) {
        Map<String, Object> error = new HashMap<>();
       //error.put("timestamp", LocalDateTime.now());
        error.put("message", ex.getMessage());
        // error.put("status", HttpStatus.BAD_REQUEST.value());
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(CustomException.class)
    public ResponseEntity<Map<String, Object>> handleCustomException(CustomException ex) {
        Map<String, Object> error = new HashMap<>();
        // error.put("timestamp", LocalDateTime.now());
        error.put("message", ex.getMessage());
        // error.put("status", HttpStatus.BAD_REQUEST.value());
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, Object>> handleValidationErrors(MethodArgumentNotValidException ex) {
        String message = ex.getBindingResult()
                           .getFieldErrors()
                           .stream()
                           .map(error -> error.getField() + ": " + error.getDefaultMessage())
                           .collect(Collectors.joining(", "));
        return new ResponseEntity<>(Collections.singletonMap("message", message), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<Map<String, Object>> handleConstraintViolations(ConstraintViolationException ex) {
        String message = ex.getConstraintViolations()
                           .stream()
                           .map(v -> v.getPropertyPath() + ": " + v.getMessage())
                           .collect(Collectors.joining(", "));
        return new ResponseEntity<>(Collections.singletonMap("message", message), HttpStatus.BAD_REQUEST);
    }
}

