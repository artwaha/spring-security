package com.atwaha.springsecurity.security;

import com.atwaha.springsecurity.Utils;
import com.atwaha.springsecurity.model.dto.ErrorResponseDTO;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

//@RestControllerAdvice
@RequiredArgsConstructor
public class GlobalExceptionHandler {
    private final Utils utils;
    private final HttpServletRequest request;

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponseDTO> handleOtherExceptions(Exception exception) {
        ErrorResponseDTO response = utils.generateErrorResponse(HttpStatus.BAD_REQUEST, request.getRequestURI(), exception.getMessage(), null);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }
}
