package com.example.demo.config;

import lombok.Getter;

/**
 * 에러 코드 관리
 */
@Getter
public enum BaseResponseStatus {
    /**
     * 1000 : 요청 성공
     */
    SUCCESS(true, 1000, "요청에 성공하였습니다."),

    SUCCESS_DELETE_USER(true, 1001, "회원 탈퇴에 성공하였습니다."),
    SUCCESS_DELETE_PRODUCT(true, 1002, "상품 삭제에 성공하였습니다"),

    SUCCESS_MODIFY_USER(true, 1003, "회원 수정에 성공하였습니다."),
    SUCCESS_MODIFY_PRODUCT(true, 1004, "상품 수정에 성공하였습니다."),
    SUCCESS_UPDATE_VIEW_COUNT(true, 1005, "조회수 증가에 성공하였습니다."),
    SUCCESS_CREATE_WISH(true, 1006, "상품 게시물 좋아요에 성공하였습니다."),
    /**
     * 2000 : Request 오류
     */
    // Common
    REQUEST_ERROR(false, 2000, "입력값을 확인해주세요."),
    EMPTY_JWT(false, 2001, "JWT를 입력해주세요."),
    INVALID_JWT(false, 2002, "유효하지 않은 JWT입니다."),
    INVALID_USER_JWT(false,2003,"권한이 없는 유저의 접근입니다."),

    // users
    USERS_EMPTY_USER_ID(false, 2010, "유저 아이디 값을 확인해주세요."),

    /* [POST] /users */
    // 1. phoneNumber
    POST_USERS_EMPTY_PHONE_NUMBER(false, 2015, "핸드폰 번호을 입력해주세요."),
    POST_USERS_INVALID_PHONE_NUMBER(false, 2016, "핸드폰 번호 형식을 확인해주세요."),
    POST_USERS_EXISTS_PHONE_NUMBER(false,2017,"중복된 핸드폰 번호입니다."),
    // 2. nickname
    POST_USERS_EMPTY_NICKNAME(false, 2015, "넥네임을 입력해주세요."),
    // 3. password
    POST_USERS_EMPTY_PASSWORD(false, 2015, "패스워드를 입력해주세요.."),
    POST_USERS_INVALID_PASSWORD(false, 2016, "패스워드 형식을 확인해주세요."),
    // 4. 탈퇴 사유
    EMPTY_DELETE_ACCOUNT_REASON(false, 2017, "탈퇴 사유를 입력해주세요"),


    /**
     * 3000 : Response 오류
     */
    // Common
    RESPONSE_ERROR(false, 3000, "값을 불러오는데 실패하였습니다."),

    // [POST] /users
    DUPLICATED_EMAIL(false, 3013, "중복된 이메일입니다."),
    FAILED_TO_LOGIN(false,3014,"없는 아이디거나 비밀번호가 틀렸습니다."),



    /**
     * 4000 : Database, Server 오류
     */
    DATABASE_ERROR(false, 4000, "데이터베이스 연결에 실패하였습니다."),
    SERVER_ERROR(false, 4001, "서버와의 연결에 실패하였습니다."),

    //[PATCH] /users/{userIdx}
    MODIFY_FAIL_USER(false,4014,"유저네임 수정 실패"),
    DELETE_FAIL_USER(false, 4015, "유저 탈퇴 실패"),

    MODIFY_FAIL_PRODUCT(false, 4016, "상품 수정 실패"),
    DELETE_FAIL_PRODUCT(false, 40117, "상품 삭제 실패"),

    UPDATE_FAIL_VIEW_COUNT(false, 4018, "상품 게시글의 조회수 증가에 실패하였습니다."),

    PASSWORD_ENCRYPTION_ERROR(false, 4011, "비밀번호 암호화에 실패하였습니다."),
    PASSWORD_DECRYPTION_ERROR(false, 4012, "비밀번호 복호화에 실패하였습니다.");




    // 5000 : 필요시 만들어서 쓰세요
    // 6000 : 필요시 만들어서 쓰세요


    private final boolean isSuccess;
    private final int code;
    private final String message;

    private BaseResponseStatus(boolean isSuccess, int code, String message) {
        this.isSuccess = isSuccess;
        this.code = code;
        this.message = message;
    }
}
