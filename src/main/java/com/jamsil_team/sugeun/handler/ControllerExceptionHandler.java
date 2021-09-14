package com.jamsil_team.sugeun.handler;

import com.jamsil_team.sugeun.handler.exception.CustomApiException;
import com.jamsil_team.sugeun.handler.exception.CustomValidationApiException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@ControllerAdvice //모든 예외를 낚아챔.
public class ControllerExceptionHandler {


    @ExceptionHandler(CustomValidationApiException.class)
    public ResponseEntity<Map<String,String>> validationApiException(CustomValidationApiException e){

        return new ResponseEntity<>(e.getErrorMap(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(CustomApiException.class)
    public ResponseEntity<String> apiException(CustomApiException e){

        return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
    }
}
