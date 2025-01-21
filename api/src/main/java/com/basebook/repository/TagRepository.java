package com.basebook.repository;

import com.basebook.model.Tag;

import java.util.List;

public interface TagRepository {


    Tag findById(long id);

    List<Tag> findAll();

    List<Tag> findTagByPost(Long postId);
}
