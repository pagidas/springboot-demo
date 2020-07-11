package me.kostasakrivos.springboot.demo.springbootdemo.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

import java.time.Instant;

public class ApiRequestException extends RuntimeException {

    public ApiRequestException(String message) {
        super(message);
    }

    public ApiRequestException(String message, Throwable cause) {
        super(message, cause);
    }
}

@ControllerAdvice
class ApiRequestExceptionHandler {

    @ExceptionHandler({ ApiRequestException.class })
    public ResponseEntity<Object> handleApiRequestException(ApiRequestException e, WebRequest request) {
        return new ResponseEntity<>(
            new ApiException(
                    e.getMessage(),
                    HttpStatus.BAD_REQUEST,
                    Instant.now()
            ),
            HttpStatus.BAD_REQUEST
        );
    }
}

class ApiException {

    final public String message;
    final public HttpStatus httpStatus;
    final public Instant timestamp;

    public ApiException(String message, HttpStatus httpStatus, Instant timestamp) {
        this.message = message;
        this.httpStatus = httpStatus;
        this.timestamp = timestamp;
    }
}

