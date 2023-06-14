package com.nhnacademy.gateway.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@AllArgsConstructor
public class ErrorCode {
    private String statusCode;
    private String statusMessage;
}
