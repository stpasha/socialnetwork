package com.basebook.repository;

import com.basebook.model.Comment;

import java.util.List;

public interface CommentRepository {

    void save(Comment comment);

    void update(Comment comment);

    void delete(long id);

    long countByPostId(long postId);
}
