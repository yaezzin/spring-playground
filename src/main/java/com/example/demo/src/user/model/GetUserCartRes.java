package com.example.demo.src.user.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class GetUserCartRes {
    private int productIdx;
    private String productName;
    private String kg;
    private String quantity;
    private String liter;
    private int price;
    private int discount;
    private int deliveryType;
    private String prodRepImageUrl;
}
