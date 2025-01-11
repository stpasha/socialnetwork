package com.basebook.model;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@ToString
@EqualsAndHashCode
@Builder
public class Comment {
    private Long id;
    private Long postId;
    private String content;
    private LocalDateTime createdAt;
}