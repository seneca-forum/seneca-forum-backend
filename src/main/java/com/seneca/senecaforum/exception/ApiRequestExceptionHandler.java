package com.seneca.senecaforum.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice

public class ApiRequestExceptionHandler extends ResponseEntityExceptionHandler {
//    @ExceptionHandler
//    public ResponseEntity<ApiExceptionResponse> handleException(BadCredentialsException e){
//        ApiExceptionResponse apiExceptionResponse = new ApiExceptionResponse(
//                e.getMessage(),
//                HttpStatus.UNAUTHORIZED.value(),
//                System.currentTimeMillis()
//        );
//        return new ResponseEntity<>(apiExceptionResponse,HttpStatus.UNAUTHORIZED);
//    }
}
