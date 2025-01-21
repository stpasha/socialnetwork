package com.basebook.service;

import com.basebook.model.Like;
import com.basebook.repository.LikeRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DefaultLikeService implements LikeService {

    private final LikeRepository likeRepository;

    public DefaultLikeService(LikeRepository likeRepository) {
        this.likeRepository = likeRepository;
    }

    @Override
    public void create(Like like) {
        likeRepository.save(like);
    }

    @Override
    public Long countLikesByPost(Long postId) {
        return likeRepository.countByPostId(postId);
    }
}
