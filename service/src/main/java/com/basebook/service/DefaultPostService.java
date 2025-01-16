package com.basebook.service;

import com.basebook.model.Post;
import com.basebook.repository.PostRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DefaultPostService implements PostService {

    private final PostRepository postRepository;

    public DefaultPostService(PostRepository postRepository) {
        this.postRepository = postRepository;
    }

    @Override
    public Post get(Long id) {
        return postRepository.findById(id);
    }

    @Override
    public List<Post> getByTag(String name) {
        return postRepository.findByTag(name);
    }

    @Override
    public void create(Post post) {
        postRepository.save(post);
    }

    @Override
    public void delete(Long id) {
        postRepository.delete(id);
    }

    @Override
    public List<Post> listPosts(int limit, int offset) {
        return postRepository.findAll(limit, offset);
    }
}
