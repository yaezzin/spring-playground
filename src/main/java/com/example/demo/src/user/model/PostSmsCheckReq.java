package com.example.demo.src.user.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
public class PostSmsCheckReq {
    private String phoneNumber;
    private String certificationNum;
}
