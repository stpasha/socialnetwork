package com.basebook.model;

import lombok.*;

import java.util.List;

@Getter
@Setter
@ToString
@EqualsAndHashCode
@Builder
public class Post {
    private Long id;
    private String title;
    private String content;
    private List<Tag> tags;  // Список тегов
    private List<Like> likes;  // Список лайков
    private List<Comment> comments;  // Список комментариев
}
