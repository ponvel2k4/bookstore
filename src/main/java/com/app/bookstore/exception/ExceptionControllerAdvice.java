package com.app.bookstore.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import lombok.RequiredArgsConstructor;

@ControllerAdvice
@RequiredArgsConstructor
public class ExceptionControllerAdvice {
	
   
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorMessage> handleException(Exception exception) {
    	ErrorMessage message = ErrorMessage.builder().message(exception.getMessage()).statusCode(HttpStatus.INTERNAL_SERVER_ERROR.value()).build();
        return new ResponseEntity<ErrorMessage>(message,HttpStatus.INTERNAL_SERVER_ERROR);
    }
    
    @ExceptionHandler(BookNotFoundException.class)
    public ResponseEntity<ErrorMessage> handleException(BookNotFoundException exception) {
    	ErrorMessage message = ErrorMessage.builder().message(exception.getMessage()).statusCode(HttpStatus.NOT_FOUND.value()).build();
        return new ResponseEntity<ErrorMessage>(message,HttpStatus.NOT_FOUND);
    }

}
