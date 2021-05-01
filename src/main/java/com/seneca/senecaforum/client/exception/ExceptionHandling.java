package com.seneca.senecaforum.client.exception;

import com.seneca.senecaforum.service.dto.ErrorResponse;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class ExceptionHandling extends ResponseEntityExceptionHandler {

    public ExceptionHandling() {
    }

    @ExceptionHandler(value = {NotFoundException.class})
    protected ResponseEntity<Object> handleNotFoundException(RuntimeException ex, WebRequest request){
        String path = ((ServletWebRequest)request).getRequest().getRequestURI();
        ErrorResponse error = new ErrorResponse(HttpStatus.NO_CONTENT.value(),ex.getMessage(),path);
        return handleExceptionInternal(ex,error,new HttpHeaders(),HttpStatus.BAD_REQUEST,request);
    }
}
