package com.basebook.persistence.repository;

import com.basebook.model.Tag;

import java.util.List;
import java.util.Optional;

public interface TagRepository {


    Optional<Tag> findById(long id);

    List<Tag> findAll();

    List<Tag> findTagByPost(Long postId);
}
