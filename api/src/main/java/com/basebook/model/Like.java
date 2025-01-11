package com.basebook.model;

import lombok.*;

@Getter
@Setter
@ToString
@EqualsAndHashCode
@Builder
public class Like {
    private Long id;
    private Long postId;
}
