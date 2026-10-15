package com.javanauta.usuario.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.javanauta.usuario.infrastructure.exceptions.ConflictException;
import com.javanauta.usuario.infrastructure.exceptions.IllegalArgumentException;
import com.javanauta.usuario.infrastructure.exceptions.ResourceNotFoundException;
import com.javanauta.usuario.infrastructure.exceptions.UnauthorizedException;
import com.javanauta.usuario.infrastructure.exceptions.dto.ErrorResponse;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.LocalDateTime;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleResourceNotFoundException(ResourceNotFoundException ex, HttpServletRequest request) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(buildError(HttpStatus.NOT_FOUND.value(),
                        ex.getMessage(),
                        request.getRequestURI(),
                        "Not Found"));
    }

    @ExceptionHandler(ConflictException.class)
    public ResponseEntity<ErrorResponse> handleConflictException(ConflictException ex, HttpServletRequest request) {
        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(buildError(HttpStatus.CONFLICT.value(),
                        ex.getMessage(),
                        request.getRequestURI(),
                        "Conflict"));
    }

    @ExceptionHandler(UnauthorizedException.class)
    public ResponseEntity<String> handleUnauthorizedException(UnauthorizedException ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<String> handleIllegalArgumentException(IllegalArgumentException ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
    }

    private ErrorResponse buildError(int status, String mensagem, String path, String error) {
        return ErrorResponse.builder()
                .timestamp(LocalDateTime.now())
                .status(status)
                .message(mensagem)
                .path(path)
                .error(error)
                .build();
    }

}
