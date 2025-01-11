package com.basebook.dao;

import com.basebook.model.Tag;

import java.util.List;

public interface TagDAO {


    void save(Tag tag);

    Tag findById(long id);

    List<Tag> findAll();

    List<Tag> findTagByPost(Long postId);

    void delete(long id);
}
