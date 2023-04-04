package com.example.telproject.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.time.ZonedDateTime;

@RestControllerAdvice
public class ManagerExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(value = {ManagerRequestException.class})
    public ResponseEntity<Object> handleNotFoundException(ManagerRequestException e) {
        HttpStatus badRequest = HttpStatus.BAD_REQUEST;
        ManagerNotFoundException managerNotFoundException = new ManagerNotFoundException(
                e.getMessage(),
                e,
                badRequest,
                ZonedDateTime.now()
                );
        return new ResponseEntity<>(managerNotFoundException, badRequest);
    }
}
