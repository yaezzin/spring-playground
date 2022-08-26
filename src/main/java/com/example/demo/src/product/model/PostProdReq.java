package com.example.demo.src.product.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PostProdReq {
    private String title;
    private String description;
    private String canProposal;
    private String productImage;
    private int price;
    private int categoryIdx;
    private int sellerIdx; // userIdx
}
