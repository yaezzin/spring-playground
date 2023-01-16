package com.example.demo.src.review.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class GetUserReviewRes {
    private int reviewIdx;
    private String productName;
    private String prodRepImageUrl; // 상품의 대표 이미지 url
    private int starPoint;
    private String createdAt; // 리뷰 생성일자
    private String title;
    private String description;
    private int delivery;
    private int quality;
    private int satisfaction;
    private String quantity;
    private String weight;
    private String liter;
}
