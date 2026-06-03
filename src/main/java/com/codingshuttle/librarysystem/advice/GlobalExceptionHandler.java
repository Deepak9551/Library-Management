package com.codingshuttle.librarysystem.advice;

import com.codingshuttle.librarysystem.exception.ResourceNotFound;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    // default exception handler
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse> handleException(Exception ex) {
        return buildApiResponse((new ApiError(ex.getMessage(),HttpStatus.INTERNAL_SERVER_ERROR,null)));
    }

    public ResponseEntity<ApiResponse> buildApiResponse(ApiError apiErrors) {
        return new ResponseEntity<>(new ApiResponse<>(apiErrors),apiErrors.getStatus());
    }

    // resource not found exception handler
    @ExceptionHandler(ResourceNotFound.class)
    public ResponseEntity<ApiResponse> handleResourceNotFoundException(ResourceNotFound ex) {
        return buildApiResponse(new ApiError(ex.getMessage(),HttpStatus.NOT_FOUND,null));
    }
}
