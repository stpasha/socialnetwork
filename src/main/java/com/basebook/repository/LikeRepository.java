package com.basebook.repository;

import com.basebook.model.Like;

public interface LikeRepository {

    void save(Like like);

    long countByPostId(long postId);
}
