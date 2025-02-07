package com.basebook.model;

import lombok.*;

@Getter
@Setter
@ToString
@EqualsAndHashCode
@Builder
public class Tag {
    private Long id;
    private String name;
}
