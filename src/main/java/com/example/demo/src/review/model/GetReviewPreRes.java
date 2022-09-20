package com.example.demo.src.review.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class GetReviewPreRes {
    private String userName;
    private String title;
    private String description;
    private int starPoint;
    private String repImage;
    private String createdAt;
    private int reviewHelpCount;
}
