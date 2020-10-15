package com.springboot.blog.exception;

import com.springboot.blog.vo.Response;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler
    public ResponseEntity<Response> handleException(Exception e) {
        Response response = new Response();
        response.setStatus(HttpStatus.BAD_REQUEST);
        response.setMessage(e.getMessage());
        response.setTimeStamp(System.currentTimeMillis());

        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

}
