package com.example.demo.src.product.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class GetProdRes {
    private int transIdx;
    private String createdAt;
    private String title;
    private int price;
    private String pulledAt; // 끌올 시간
    private String repImage; // 대표 이미지
    private String regionGu; // 구
    private String regionTown; // 동, 가
    private int wishCount;
}
