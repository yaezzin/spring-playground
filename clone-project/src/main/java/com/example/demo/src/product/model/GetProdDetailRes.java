package com.example.demo.src.product.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class GetProdDetailRes {
    private int productIdx;
    private String createdAt;
    private String title;
    private String description;
    private String pulledAt;
    private int price;
    private int wishCount;
    private int viewCount;
    private String canProposal; // 제안 가능여부
    private int sellerIdx;
    private String profileImage;
    private String nickname;
    private String regionGu;
    private String regionTown;
    private int mannerTemp;

}
