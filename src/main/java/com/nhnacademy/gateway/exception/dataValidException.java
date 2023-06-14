package com.nhnacademy.gateway.exception;

import org.springframework.validation.BindingResult;

import java.util.stream.Collectors;

public class dataValidException extends RuntimeException{
    public dataValidException(BindingResult bindingResult){
        super(bindingResult.getAllErrors()
                .stream()
                .map(objectError -> new StringBuilder()
                        .append(objectError.getDefaultMessage()))
                .collect(Collectors.joining("-")));
    }
}
