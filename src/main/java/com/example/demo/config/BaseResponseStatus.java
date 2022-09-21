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
    SUCCESS_MODIFY_USER_PASSWORD(true, 1001, "비밀번호 수정에 성공하였습니다."),
    SUCCESS_MODIFY_USER_PROFILE(true, 1002, "프로필 사진 변경에 성공하였습니다."),
    SUCCESS_CREATE_CATEGORY(true, 1003, "카테고리 생성에 성공하였습니다."),
    SUCCESS_MODIFY_CATEGORY(true, 1004, "카테고리 수정에 성공하였습니다."),
    SUCCESS_MODIFY_REVIEW(true, 1005, "리뷰 수정에 성공하였습니다."),
    SUCCESS_REVIEW_HELP(true, 1006, "리뷰의 도움이 됐어요 등록/해제에 성공하였습니다."),
    SUCCESS_DELETE_REVIEW(true, 1007, "리뷰 삭제에 성공하였습니다."),
    SUCCESS_CREATE_WISH(true, 1008, "상품 찜 등록에 성공하였습니다"),
    SUCCESS_DELETE_WISH(true, 1009, "상품 찜 해제에 성공하였습니다."),
    SUCCESS_CREATE_CART(true, 1010, "상품의 장바구니 등록에 성공하였습니다."),

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
    USERS_EMPTY_EMAIl(false, 2011, "존재하지 않는 이메일입니다."),

    POST_USERS_EMPTY_EMAIL(false, 2015, "이메일을 입력해주세요."),
    POST_USERS_INVALID_EMAIL(false, 2016, "이메일 형식을 확인해주세요."),
    POST_USERS_EXISTS_EMAIL(false,2017,"중복된 이메일입니다."),
    POST_USERS_EMPTY_PHONE_NUMBER(false, 2018, "핸드폰 번호를 입력해주세요."),
    POST_USERS_EXISTS_PHONE_NUMBER(false, 2019, "중복된 핸드폰 번호입니다."),
    POST_USERS_EMPTY_NAME(false, 2020, "유저의 이름을 입력해주세요."),
    POST_USERS_EMPTY_PASSWORD(false, 2021, "비밀번호를 입력해주세요"),
    POST_USERS_INVALID_PASSWORD(false, 2022, "비밀번호 형식을 확인해주세요"),
    POST_USERS_EMAIL_IN_PASSWORD(false, 2023, "비밀번호에는 이메일을 입력할 수 없습니다."),

    // product
    EMPTY_PRODUCT_IDX(false, 2030, "상품 식별자가 존재하지 않습니다."),
    EMPTY_PRODUCT(false, 2032, "상품 게시글이 존재하지 않습니다"),
    POST_PRODUCT_WISH_EXIST(false, 2033, "상품에 대한 찜을 이미 등록하였습니다."),
    POST_PRODUCT_WISH_NOT_EXIST(false, 2034, "상품에 대한 찜을 이미 해제하였습니다"),

    // category
    EMPTY_CATEGORY_IDX(false, 2031, "카테고리 식별자가 존재하지 않습니다."),

    //review
    POST_REVIEW_HELP_EXIST(false, 2040, "이미 도움이 요청했거나, 요청하지 않은 상태입니다."),
    EMPTY_REVIEW(false, 2041, "리뷰가 존재하지 않습니다."),


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
    MODIFY_FAIL_USERNAME(false,4014,"유저네임 수정 실패"),
    MODIFY_FAIL_USER_PROFILE(false, 4018, "유저 프로필 수정에 실패하였습니다."),

    MODIFY_FAIL_USER_PASSWORD(false, 4015, "비밀번호 수정에 실패하였습니다."),
    USER_CURRENT_PASSWORD_NOT_CORRECT(false, 4016, "현재 비밀번호가 일치하지 않습니다."),
    USER_NEW_PASSWORD_NOT_CORRECT(false, 4017, "새 비밀번호가 일치하지 않습니다."),

    PASSWORD_ENCRYPTION_ERROR(false, 4011, "비밀번호 암호화에 실패하였습니다."),
    PASSWORD_DECRYPTION_ERROR(false, 4012, "비밀번호 복호화에 실패하였습니다."),

    // [Category]
    CREATE_FAIL_CATEGORY(false, 4020, "카테고리 생성에 실패하였습니다"),
    MODIFY_FAIL_CATEGORY(false, 4021, "카테고리 수정에 실패하였습니다."),

    // [Review]
    CREATE_FAIL_REVIEW(false, 4030, "리뷰 생성에 실패하였습니다."),
    MODIFY_FAIL_REVIEW(false, 4031, "리뷰 수정에 실패하였습니다."),
    CREATE_FAIL_REVIEW_HELP(false, 4032, "도움이 됐어요 등록에 실패하였습니다"),
    UPDATE_FAIL_REVIEW_HELP(false, 4044, "도움이 됐어요 수정에 실패하였습니다"),
    DELETE_FAIL_EXISTS_REVIEW_HELP(false, 4033, "도움이 됐어요 해제에 실패하였습니다."),
    DELETE_FAIL_REVIEW(false, 4035, "리뷰 삭제에 실패하였습니다."),

    // product
    CREATE_FAIL_PRODUCT_WISH(false, 4040, "상품 찜 등록에 실패하였습니다."),
    PUT_FAIL_PRODUCT_WISH(false, 4041, "상품 찜 해제에 실패하였습니다"),

    //order
    CREATE_FAIL_CART(false, 4050, "상품의 장바구니 등록에 실패하였습니다.");

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
