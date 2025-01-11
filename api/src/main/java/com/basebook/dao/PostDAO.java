package com.basebook.dao;

import com.basebook.model.Post;

import java.util.List;

public interface PostDAO {

    void save(Post post);

    Post findById(long id);

    List<Post> findAll();

    List<Post> findByTag(String tag);

    void update(Post post);

    void delete(long id);
}
