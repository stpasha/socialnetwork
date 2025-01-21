package com.basebook.service;

import com.basebook.model.Like;

import java.util.List;

public interface LikeService {

    void create(Like like);

    Long countLikesByPost(Long postId);

}
