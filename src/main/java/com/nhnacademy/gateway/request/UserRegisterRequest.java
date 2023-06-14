package com.nhnacademy.gateway.request;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Getter
@Setter
public class UserRegisterRequest {
    @NotBlank(message = "아이디를 입력하세요")
    @Size(min = 1,max = 20, message = "아이디는 1~20 사이로 작성해주세요.")
    private String userId;

    @NotBlank(message = "패스워드를 입력하세요")
    @Size(min = 1,max = 20, message = "패스워드는 1~20 사이로 작성해주세요.")
    private String password;

    @NotBlank(message = "이메일를 입력하세요")
    @Size(min = 1,max = 40, message = "이메일은 1~40 사이로 작성해주세요.")
    private String email;

}
