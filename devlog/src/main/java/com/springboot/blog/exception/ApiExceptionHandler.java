package com.springboot.blog.exception;

import com.springboot.blog.api.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ApiExceptionHandler {

    @ExceptionHandler
    public ResponseEntity<ApiResponse> handleException(Exception e) {
        HttpStatus badRequest = HttpStatus.BAD_REQUEST;
        ApiResponse apiResponse = new ApiResponse(badRequest, e.getMessage(), System.currentTimeMillis());

        return new ResponseEntity<>(apiResponse, badRequest);
    }

}
