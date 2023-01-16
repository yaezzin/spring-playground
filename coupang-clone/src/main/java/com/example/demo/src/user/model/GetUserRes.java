package com.example.demo.src.user.model;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class GetUserRes {
    private int userIdx;
    private String email;
    private String password;
    private String userName;
    private String phoneNumber;
    private String profileImage;
    private String status;
    private String createdAt;
}
