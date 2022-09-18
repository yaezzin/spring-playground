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

    // [POST] /users
    POST_USERS_EMPTY_EMAIL(false, 2015, "이메일을 입력해주세요."),
    POST_USERS_INVALID_EMAIL(false, 2016, "이메일 형식을 확인해주세요."),
    POST_USERS_EXISTS_EMAIL(false,2017,"중복된 이메일입니다."),

    POST_USERS_EMPTY_PHONE_NUMBER(false, 2018, "핸드폰 번호를 입력해주세요."),
    POST_USERS_EXISTS_PHONE_NUMBER(false, 2019, "중복된 핸드폰 번호입니다."),

    POST_USERS_EMPTY_NAME(false, 2020, "유저의 이름을 입력해주세요."),
    POST_USERS_EMPTY_PASSWORD(false, 2021, "비밀번호를 입력해주세요"),
    POST_USERS_INVALID_PASSWORD(false, 2022, "비밀번호 형식을 확인해주세요"),
    POST_USERS_EMAIL_IN_PASSWORD(false, 2023, "비밀번호에는 이메일을 입력할 수 없습니다."),



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
    CREATRE_FAIL_REVIEW(false, 4030, "리뷰 생성에 실패하였습니다.");


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
