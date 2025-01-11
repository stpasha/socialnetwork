package com.basebook.service;

import com.basebook.model.Like;

import java.util.List;

public interface LikeService {
    Like get(Long id);

    void create(Like like);

    void delete(Long id);

    List<Like> getLikesByPost(Long postId);

}
