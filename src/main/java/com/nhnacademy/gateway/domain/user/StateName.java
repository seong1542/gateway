package com.nhnacademy.gateway.domain.user;

import lombok.Getter;

import javax.validation.Valid;

public enum StateName {
    JOINED("가입"), DORMANT("휴면"), CANCELED("탈퇴");
    @Getter
    private final String statusKoreaName;
    StateName(String statusKoreaName){
        this.statusKoreaName = statusKoreaName;
    }

}
