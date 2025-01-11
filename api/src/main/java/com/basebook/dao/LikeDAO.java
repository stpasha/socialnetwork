package com.basebook.dao;

import com.basebook.model.Like;

public interface LikeDAO {

    void save(Like like);

    long countByPostId(long postId);

    void delete(long id);

    void deleteByPostId(long postId);
}
