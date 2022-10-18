package com.yaezzin.webservice.web.dto;

import com.yaezzin.webservice.web.domain.posts.Posts;
import lombok.Getter;

@Getter
public class PostsResponseDto {

    private Long id;
    private String title;
    private String content;
    private String author;

    public PostsResponseDto(Posts posts) { //엔티티 필드 중 일부만 사용 -> 엔티티에서 받아 값을 넣음
        this.id = posts.getId();
        this.title = posts.getTitle();
        this.content = posts.getContent();
        this.author = posts.getAuthor();
    }

}
