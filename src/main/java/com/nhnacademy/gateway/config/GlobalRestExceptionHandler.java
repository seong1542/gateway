package com.nhnacademy.gateway.config;

import com.nhnacademy.gateway.domain.ErrorCode;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.client.HttpClientErrorException;
@RestControllerAdvice
public class GlobalRestExceptionHandler {
    @ExceptionHandler(HttpClientErrorException.class)
    public String getHttpClientErrorException(HttpClientErrorException clientErrorException,
                                              Model model){
        model.addAttribute("statusCode", clientErrorException.getStatusCode());
        model.addAttribute("statusMessage", clientErrorException.getMessage());
        return "errorPage";
    }
}