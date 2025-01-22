package com.basebook.service;

import com.basebook.model.Like;

public interface LikeService {

    void create(Like like);

    Long countLikesByPost(Long postId);

}
