package com.basebook.dao;

import com.basebook.model.Comment;

import java.util.List;

public interface CommentDAO {

    void save(Comment comment);

    Comment findById(long id);

    List<Comment> findByPostId(long postId);


    List<Comment> findAll();

    void update(Comment comment);

    void delete(long id);

    long countByPostId(long postId);
}
