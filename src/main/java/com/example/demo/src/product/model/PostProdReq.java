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
public class PostProdReq {
    private String productName;
    private String discount;
    private String deliveryAt; // 배송사
    private int deliveryTip; // 배달비
    private int categoryIdx; // 카테고리 ID
    private int sellerIdx; // 판매자 ID
    private int productIdx;
    private List<String> prodRepImageUrl; //대표 이미지
    private List<String> prodContentImageUrl; // 상품 설명 이미지
}
