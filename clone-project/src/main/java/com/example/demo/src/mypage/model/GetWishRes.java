package com.example.demo.src.mypage.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class GetWishRes {
    private String title;
    private int price;
    private String repImage;
    private String regionTown;
    private String wishCount;
}
