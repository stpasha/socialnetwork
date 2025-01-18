package com.basebook.repository;

import com.basebook.model.Post;

import java.util.List;

public interface PostRepository {

    void save(Post post);

    Post findById(long id);

    List<Post> findAll(int limit, int offset, String filter);

//    List<Post> findByTag(String tag);

    void update(Post post);

    void delete(long id);
}
