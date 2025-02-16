package com.basebook.service;

import com.basebook.model.Post;
import com.basebook.model.PostList;

import java.util.List;
import java.util.Optional;

public interface PostService {
    Optional<Post> get(Long id);

    void create(Post post);

    void update(Post post);

    void delete(Long id);

    List<PostList> listPosts(int limit, int offset, String filter);
}
