package com.basebook.service;


import com.basebook.model.Comment;
import com.basebook.repository.CommentRepository;
import org.springframework.stereotype.Service;

@Service
public class DefaultCommentService implements CommentService {

    private final CommentRepository commentRepository;

    public DefaultCommentService(final CommentRepository commentRepository) {
        this.commentRepository = commentRepository;
    }

    @Override
    public void create(final Comment comment) {
        commentRepository.save(comment);
    }

    @Override
    public void delete(final Long id) {
        commentRepository.delete(id);
    }

    @Override
    public void update(final Comment comment) {
        commentRepository.update(comment);
    }

}
