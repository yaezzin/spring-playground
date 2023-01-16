package com.example.demo.src.mypage.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class GetMyProdRes {
    private int productIdx;
    private String title;
    private int price;
    private String createdAt;
    private String repImage;
    private String regionTown;
    private int chatCount;
}
