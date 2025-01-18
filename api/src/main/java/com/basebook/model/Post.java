package com.basebook.model;

import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@ToString
@EqualsAndHashCode
@Builder
//@NoArgsConstructor
@AllArgsConstructor
public class Post {
    private Long id;
    private String title;
    private String content;
    private String imageUrl;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private boolean isDeleted;
    private List<Tag> tags;
    private List<Comment> comments;
    private List<Like> likes;
    private Integer likesCount;
    private Integer commentsCount;

    public int getLikesCount() {
        if (likesCount == null) {
            if (likes != null) {
                return likes.size();
            } else {
                return 0;
            }
        }
        return likesCount;
    }

    public int getCommentsCount() {
        if (commentsCount == null) {
            if (comments != null) {
                return comments.size();
            } else {
                return 0;
            }
        }
        return commentsCount;
    }

    public void setLikesCount(Integer likesCount) {
        this.likesCount = likesCount;
    }

    public void setCommentsCount(Integer commentsCount) {
        this.commentsCount = commentsCount;
    }
}