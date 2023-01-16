package com.example.demo.src.chat.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class GetChatRes {
    private int chatRoomIdx;
    private String recieverNickname;
    private String profileImage;
    private String message;
    private String createdAt;
    private int productIdx;
    private String productImage;
    private String regionTown;
}
