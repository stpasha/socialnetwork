package com.basebook.persistence.repository;

import com.basebook.model.Post;
import com.basebook.model.PostList;

import java.util.List;
import java.util.Optional;

public interface PostRepository {

    void save(Post post);

    Optional<Post> findById(long id);

    List<PostList> findAll(int limit, int offset, String filter);

//    List<Post> findByTag(String tag);

    void update(Post post);

    void delete(long id);
}
