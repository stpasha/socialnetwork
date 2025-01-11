package com.basebook.service;

import com.basebook.model.Comment;

import java.util.List;

public interface CommentService {
    Comment get(Long id);

    void create(Comment comment);

    void delete(Long id);

    List<Comment> getCommentsByPost(Long postId);
}
