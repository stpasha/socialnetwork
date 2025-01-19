package com.basebook.repository;

import com.basebook.model.Post;

import java.util.List;
import java.util.Optional;

public interface PostRepository {

    void save(Post post);

    Optional<Post> findById(long id);

    List<Post> findAll(int limit, int offset, String filter);

//    List<Post> findByTag(String tag);

    void update(Post post);

    void delete(long id);
}
