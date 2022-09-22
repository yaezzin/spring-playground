package com.example.demo.src.product.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class GetProdDetailRes {
    private int productIdx;
    private String productName;
    private int discount;
    private int price;
    private String deliveryAt;
    private String sellerName; //sellerIdx로 가져와야함
    private int starPoint; // 별점 평균
    private int reviewCount; // 총 리뷰 개수
    private String quantity;
    private String kg;
    private String liter;
    private List<String> prodRepImageUrl;
    private List<String> prodContentImageUrl;

    public GetProdDetailRes(int productIdx, String productName, int discount, int price, String deliveryAt, String sellerName,
                            int starPoint, int reviewCount, String quantity, String kg, String liter) {
        this.productIdx = productIdx;
        this.productName = productName;
        this.discount = discount;
        this.price = price;
        this.deliveryAt = deliveryAt;
        this.sellerName = sellerName;
        this.starPoint = starPoint;
        this.reviewCount = reviewCount;
        this.quantity = quantity;
        this.kg = kg;
        this.liter = liter;
    }

}
