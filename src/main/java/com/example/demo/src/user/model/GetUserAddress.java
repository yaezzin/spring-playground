package com.example.demo.src.user.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class GetUserAddress {
    private String recipient;
    private String phoneNumber;
    private String isDefaultAddress; //기본 배송지여부
    private String address;
    private String addressDetail; //상세 주소
    private int deliveryRequest; // 요청 정보
}
