package com.jamsil_team.sugeun.handler.aop;

import com.jamsil_team.sugeun.handler.exception.CustomValidationApiException;
import com.jamsil_team.sugeun.security.dto.AuthUserDTO;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

@Component
@Aspect
public class ValidationAdvice {

    @Around("execution(* com.jamsil_team.sugeun.controller.*Controller.*(..))")
    public Object apiAdvice(ProceedingJoinPoint proceedingJoinPoint) throws Throwable{

        Object[] args = proceedingJoinPoint.getArgs();

        for(Object arg : args){
            if(arg instanceof BindingResult){

                BindingResult bindingResult = (BindingResult) arg;

                if(bindingResult.hasErrors()){
                    Map<String, String> errorMap = new HashMap<>();

                    for(FieldError error : bindingResult.getFieldErrors()){
                        errorMap.put(error.getField(), error.getDefaultMessage());
                        System.out.println(error.getDefaultMessage());
                    }
                    throw new CustomValidationApiException("유효성검사 실패함", errorMap);
                }
            }
        }

        return proceedingJoinPoint.proceed();
    }
}
