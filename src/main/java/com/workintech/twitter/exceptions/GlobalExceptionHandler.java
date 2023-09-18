package com.workintech.twitter.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;

@ControllerAdvice
public class GlobalExceptionHandler {
    public ResponseEntity<TweetErrorResponse> handleException(TweetErrorException exception){
        TweetErrorResponse response = new TweetErrorResponse(exception.getHttpStatus().value(),
                exception.getMessage(), System.currentTimeMillis());
        return new ResponseEntity<>(response, exception.getHttpStatus());
    }

    public ResponseEntity<TweetErrorResponse> handleException(Exception exception){
        TweetErrorResponse response = new TweetErrorResponse(HttpStatus.BAD_REQUEST.value(),
                exception.getMessage(), System.currentTimeMillis());
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

}
