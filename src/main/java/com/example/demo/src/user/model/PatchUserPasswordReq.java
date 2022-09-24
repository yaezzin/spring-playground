package com.example.demo.src.user.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class PatchUserPasswordReq {
    private String currentPassword; //현재 비밀번호
    private String modPassword; // 새 비밀번호
    private String reModPassword; // 새 비밀번호 한번 더 확인
}
