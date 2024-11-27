package com.trollyworld.bootapp.exception;

import com.trollyworld.bootapp.dto.APIExceptionResponse;
import jakarta.validation.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    ResponseEntity<Map<String, String>> handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        Map<String, String> response = new HashMap<>();
        e.getBindingResult().getAllErrors().forEach(err -> {
            String fieldName = ((FieldError) err).getField();
            String errorMessage = err.getDefaultMessage();
            response.put(fieldName, errorMessage);
        });
        response.put("statusCode", String.valueOf(HttpStatus.BAD_REQUEST.value()));
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    ResponseEntity<Map<String,String>> handleConstraintViolationException(ConstraintViolationException e) {
        Map<String, String> response = new HashMap<>();
        e.getConstraintViolations().forEach(err -> {
            String fieldName = err.getPropertyPath().toString();
            String errMsg = err.getMessage();
            response.put(fieldName,errMsg);
        });
        response.put("statusCode", String.valueOf(HttpStatus.BAD_REQUEST.value()));
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    ResponseEntity<APIExceptionResponse> handleResourceNotFoundException(ResourceNotFoundException e) {
        String response = e.getMessage();
        APIExceptionResponse apiExceptionResponse = new APIExceptionResponse(response, false);
        return new ResponseEntity<>(apiExceptionResponse, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(APIException.class)
    ResponseEntity<APIExceptionResponse> handleAPIException(APIException e) {
        String response = e.getMessage();
        APIExceptionResponse apiExceptionResponse = new APIExceptionResponse(response, false);
        return new ResponseEntity<>(apiExceptionResponse, HttpStatus.BAD_REQUEST);
    }

}
