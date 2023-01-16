package com.example.demo.src.review.model;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class GetReviewRes {
    private int reviewIdx;
    private String userName;
    private String profileImage;
    private String productName;
    private String title;
    private String description;
    private String updatedAt;
    private int starPoint;
    private String repImage;
    private List<String> reviewImageUrl;
    private int satisfaction;
    private int delivery;
    private int quality;

    public GetReviewRes(int reviewIdx, String userName, String profileImage, String productName, String title,
                        String description, String updatedAt, int starPoint, String repImage, int satisfaction, int delivery, int quality) {
        this.reviewIdx = reviewIdx;
        this.userName = userName;
        this.profileImage = profileImage;
        this.productName = productName;
        this.title = title;
        this.description = description;
        this.updatedAt = updatedAt;
        this.starPoint = starPoint;
        this.repImage = repImage;
        this.satisfaction = satisfaction;
        this.delivery = delivery;
        this.quality = quality;
    }

}
