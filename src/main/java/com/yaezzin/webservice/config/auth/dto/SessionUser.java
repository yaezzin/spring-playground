package com.yaezzin.webservice.config.auth.dto;

import com.yaezzin.webservice.web.domain.user.User;
import lombok.Getter;

@Getter
public class SessionUser {

    // SessionUser는 인증된 사용자 정보만 필요

    private String name;
    private String email;
    private String picture;

    public SessionUser(User user) {
        this.name = user.getName();
        this.email = user.getEmail();
        this.picture = user.getPicture();
    }

}
