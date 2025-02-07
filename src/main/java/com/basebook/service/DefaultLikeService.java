package com.basebook.service;

import com.basebook.model.Like;
import com.basebook.persistence.repository.LikeRepository;
import org.springframework.stereotype.Service;

@Service
public class DefaultLikeService implements LikeService {

    private final LikeRepository likeRepository;

    public DefaultLikeService(final LikeRepository likeRepository) {
        this.likeRepository = likeRepository;
    }

    @Override
    public void create(final Like like) {
        likeRepository.save(like);
    }

    @Override
    public Long countLikesByPost(final Long postId) {
        return likeRepository.countByPostId(postId);
    }
}
