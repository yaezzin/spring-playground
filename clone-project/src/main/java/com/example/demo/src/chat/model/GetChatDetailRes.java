package com.example.demo.src.chat.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class GetChatDetailRes {
    private int chatRoomIdx;
    private String senderNickname;
    private String recieverNickname;
    private int mannerTemp;
    private String profileImage;
    private String productImage;
    private String createdAt;
    private String title;
    private int price;
    private String canProposal;
    private String message;
}
