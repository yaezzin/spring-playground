package com.example.demo.src.user.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class PostUserAddressReq {
    private String recipient;
    private String phoneNumber;
    private String isDefaultAddress; //기본 배송지여부
    private String address;
    private String addressDetail; //상세 주소
    private int deliveryRequest; // 요청 정보
    private String zipCode; //우편번호
    private String doorCode; // 공동 현관 출입번호
    private int userIdx;
}
