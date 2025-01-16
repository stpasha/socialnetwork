package com.basebook.model;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@ToString
@EqualsAndHashCode
@Builder
public class Like {
    private Long id;
    private Long postId;
    private LocalDateTime createdAt;
    private boolean isDeleted;
}