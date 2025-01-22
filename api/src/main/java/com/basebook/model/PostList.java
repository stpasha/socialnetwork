package com.basebook.model;

import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@ToString
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
public class PostList extends Post {
    private Integer likesCount;
    private Integer commentsCount;
    private String tagNames;

    public PostList(Long id, String title, String content, String imageUrl, LocalDateTime createdAt, LocalDateTime updatedAt, boolean isDeleted, List<Tag> tags, List<Comment> comments, List<Like> likes, Integer likesCount, Integer commentsCount, String tagNames) {
        super(id, title, content, imageUrl, createdAt, updatedAt, isDeleted, tags, comments, likes);
        this.likesCount = likesCount;
        this.commentsCount = commentsCount;
        this.tagNames = tagNames;
    }

    public int getLikesCount() {
        if (likesCount == null) {
            if (getLikes() != null) {
                return getLikes().size();
            } else {
                return 0;
            }
        }
        return likesCount;
    }

    public int getCommentsCount() {
        if (commentsCount == null) {
            if (getComments() != null) {
                return getComments().size();
            } else {
                return 0;
            }
        }
        return commentsCount;
    }

    public static class PostListBuilder {

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
        private String tagNames;

        public static PostList.PostListBuilder postListBuilder() {
            return new PostListBuilder();
        }


        public PostListBuilder id(Long id) {
            this.id = id;
            return this;
        }

        public PostListBuilder title(String title) {
            this.title = title;
            return this;
        }

        public PostListBuilder content(String content) {
            this.content = content;
            return this;
        }

        public PostListBuilder imageUrl(String imageUrl) {
            this.imageUrl = imageUrl;
            return this;
        }

        public PostListBuilder createdAt(LocalDateTime createdAt) {
            this.createdAt = createdAt;
            return this;
        }

        public PostListBuilder updatedAt(LocalDateTime updatedAt) {
            this.updatedAt = updatedAt;
            return this;
        }

        public PostListBuilder isDeleted(boolean isDeleted) {
            this.isDeleted = isDeleted;
            return this;
        }

        public PostListBuilder tags(List<Tag> tags) {
            this.tags = tags;
            return this;
        }

        public PostListBuilder likes(List<Like> likes) {
            this.likes = likes;
            return this;
        }

        public PostListBuilder comments(List<Comment> comments) {
            this.comments = comments;
            return this;
        }


        public PostListBuilder likesCount(Integer likesCount) {
            this.likesCount = likesCount;
            return this;
        }

        public PostListBuilder commentsCount(Integer commentsCount) {
            this.commentsCount = commentsCount;
            return this;
        }

        public PostListBuilder tagNames(String tagNames) {
            this.tagNames = tagNames;
            return this;
        }

        public PostList build() {
            return new PostList(id, title, content, imageUrl, createdAt, updatedAt, isDeleted, tags, comments, likes, likesCount, commentsCount, tagNames);
        }
    }
}
