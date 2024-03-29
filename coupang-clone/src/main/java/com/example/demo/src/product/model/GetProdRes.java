package com.example.demo.src.product.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class GetProdRes {
    private int productIdx;
    private String prodRepImageUrl;
    private String productName;
    private String deliveryType; // 배송 타입
    private int price;
    private int discount;
    private int starPoint;
    private int reviewCount;
    private boolean overnightDelivery; //당일배송유무
}
