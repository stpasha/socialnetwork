package com.basebook.service;

import com.basebook.model.Comment;

import java.util.List;

public interface CommentService {
    Comment get(Long id);

    void create(Comment comment);

    void delete(Long id);

    void update(Comment comment);

    List<Comment> getCommentsByPost(Long postId);
}
