package com.example.demo.src.review.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class PatchReviewReq {
    private int starPoint;
    private String title;
    private String description;
    private String repImage;
    private List<String> reviewImageUrl;
    private int satisfaction;
    private int delivery;
    private int quality;
    private int reviewIdx;
    private String modifiedFlag; // 이미지의 수정이 있는지 체크
}
