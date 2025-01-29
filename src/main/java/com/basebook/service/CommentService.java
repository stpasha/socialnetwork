package com.basebook.service;

import com.basebook.model.Comment;

public interface CommentService {

    void create(Comment comment);

    void delete(Long id);

    void update(Comment comment);

}
