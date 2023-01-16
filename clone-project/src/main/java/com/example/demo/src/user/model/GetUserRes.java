package com.example.demo.src.user.model;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class GetUserRes {
    private int userIdx;
    private String status;
    private String createdAt;
    private String nickname;
    private String phoneNumber;
    private String profileImage;
}
