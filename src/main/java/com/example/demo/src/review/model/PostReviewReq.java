package com.example.demo.src.review.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class PostReviewReq {
    private String title;
    private String description;
    private int starPoint;
    private int satisfaction;
    private int quality;
    private int delivery;
    private String repImage; // 유저가 첫번째로 선택한 사진을 대표이미지로 설정
    private List<String> reviewImageUrl;
    private int productIdx;
    private int userIdx;
    private int reviewIdx;
}
