package com.nhnacademy.gateway.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@AllArgsConstructor
public class UserResponse {
    private String userId;
    private String password;
    private String status;
    private String email;


}
