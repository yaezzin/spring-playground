package com.example.demo.src.board.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class PatchBoardReq {
    private int categoryIdx;
    private String content;
    private int boardIdx;
}
