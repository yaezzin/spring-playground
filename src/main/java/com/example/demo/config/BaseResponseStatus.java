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
    SUCCESS_CREATE_WISH(true, 1001, "상품 게시물 좋아요에 성공하였습니다."),
    SUCCESS_CREATE_BADGE(true, 1002, "유저 활동 배지 생성에 성공하였습니다."),
    SUCCESS_CREATE_CHAT(true, 1003, "채팅 생성에 성공하였습니다."),

    SUCCESS_MODIFY_USER(true, 1004, "회원 수정에 성공하였습니다."),
    SUCCESS_MODIFY_PRODUCT(true, 1005, "상품 수정에 성공하였습니다."),
    SUCCESS_MODIFY_STATUS(true, 1006, "상품 상태 수정에 성공하였습니다."),
    SUCCESS_UPDATE_VIEW_COUNT(true, 1007, "조회수 증가에 성공하였습니다."),
    SUCCESS_PULL_PRODUCT(true, 1008, "상품 끌올에 성공하였습니다."),
    SUCCESS_MODIFY_BOARD(true, 1009, "동네 생활 게시글 수정에 성공하였습니다."),

    SUCCESS_DELETE_USER(true, 1010, "회원 탈퇴에 성공하였습니다."),
    SUCCESS_DELETE_PRODUCT(true, 1011, "상품 삭제에 성공하였습니다"),
    SUCCESS_DELETE_BOARD(true, 1012, "동네 생활 게시글 삭제에 성공하였습니다." ),
    SUCCESS_DELETE_WISH(true, 1013, "상품 게시물 좋아요 해제에 성공하였습니다."),


    /**
     * 2000 : Request 오류
     */

    // Common
    REQUEST_ERROR(false, 2000, "입력값을 확인해주세요."),
    EMPTY_JWT(false, 2001, "JWT를 입력해주세요."),
    INVALID_JWT(false, 2002, "유효하지 않은 JWT입니다."),
    INVALID_USER_JWT(false,2003,"권한이 없는 유저의 접근입니다."),

    // users
    USERS_EMPTY_USER_ID(false, 2004, "유저 아이디 값을 확인해주세요."),

    /* [POST] /users */
    // 1. phoneNumber
    POST_USERS_EMPTY_PHONE_NUMBER(false, 2010, "핸드폰 번호를 입력해주세요."),
    POST_USERS_INVALID_PHONE_NUMBER(false, 2011, "핸드폰 번호 형식을 확인해주세요."),
    POST_USERS_EXISTS_PHONE_NUMBER(false,2012,"중복된 핸드폰 번호입니다."),
    // 2. nickname
    POST_USERS_EMPTY_NICKNAME(false, 2013, "넥네임을 입력해주세요."),
    // 3. password
    POST_USERS_EMPTY_PASSWORD(false, 2014, "패스워드를 입력해주세요."),
    POST_USERS_INVALID_PASSWORD(false, 2015, "패스워드 형식을 확인해주세요."),
    // 4. 탈퇴 사유
    EMPTY_DELETE_ACCOUNT_REASON(false, 2016, "탈퇴 사유를 입력해주세요"),


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

    //[CREATE]
    CREATE_FAIL_BADGE(false, 4013, "유저 활동 배지 생성 실패"),

    //[PATCH] /users/{userIdx}
    MODIFY_FAIL_USER(false,4014,"유저네임 수정 실패"),
    MODIFY_FAIL_PRODUCT(false, 4015, "상품 수정 실패"),
    MODIFY_FAIL_BOARD(false, 4016, "동네 생활 게시글 수정 실패"),
    MODIFY_FAIL_COMMENT(false, 4017, "댓글 수정 실패" ),

    //[DELETE]
    DELETE_FAIL_USER(false, 4020, "유저 탈퇴 실패"),
    DELETE_FAIL_PRODUCT(false, 4021, "상품 삭제 실패"),
    DELETE_FAIL_BOARD(false, 4022, "동네 생활 게시글 삭제 실패"),

    //[UPDATE]
    UPDATE_FAIL_VIEW_COUNT(false, 4030, "상품 게시글의 조회수 증가에 실패하였습니다."),
    UPDATE_FAIL_PULL(false, 4031, "상품 끌올에 실패하였습니다."),
    UPDATE_FAIL_STATUS(false, 4032, "상품 상태 업데이트에 실패하였습니다."),

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
