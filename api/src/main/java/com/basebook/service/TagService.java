package com.basebook.service;

import com.basebook.model.Tag;

import java.util.List;
import java.util.Optional;

public interface TagService {

    Optional<Tag> get(Long id);

    List<Tag> getAll();

    List<Tag> getTagsByPost(Long postId);

}
