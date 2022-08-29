package com.example.demo.src.board.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class GetBoardDetailRes {
    private int boardIdx;
    private String createdAt;
    private String content;
    private String nickname;
    private String profileImage;
    private int authCount;
    private String regionTown;
    private String categoryName;

}
