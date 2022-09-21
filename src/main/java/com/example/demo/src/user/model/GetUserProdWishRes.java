package com.example.demo.src.user.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class GetUserProdWishRes {
    private int productIdx;
    private String productName;
    private int price;
    private int discount;
    private String prodRepImageUrl; //상품 대표이미지
    private String quantity;
    private String kg;
    private String liter;
}
