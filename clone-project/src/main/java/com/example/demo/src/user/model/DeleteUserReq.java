package com.example.demo.src.user.model;

import lombok.*;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DeleteUserReq {
    private int userIdx;
    private String deleteReason; // 탈퇴 사유
}
