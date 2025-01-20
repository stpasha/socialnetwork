package com.basebook.service;

import com.basebook.model.Post;

import java.util.List;
import java.util.Optional;

public interface PostService {
    Optional<Post> get(Long id);

//    List<Post> getByTag(String name);

    void create(Post post);

    void update(Post post);

    void delete(Long id);

    List<Post> listPosts(int limit, int offset, String filter);
}
