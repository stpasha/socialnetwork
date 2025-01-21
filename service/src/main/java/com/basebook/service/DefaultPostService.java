package com.basebook.service;

import com.basebook.model.Post;
import com.basebook.model.PostList;
import com.basebook.repository.PostRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class DefaultPostService implements PostService {

    private final PostRepository postRepository;

    private final TagService tagService;

    public DefaultPostService(PostRepository postRepository,
                              TagService tagService) {
        this.postRepository = postRepository;
        this.tagService = tagService;
    }

    @Override
    public Optional<Post> get(Long id) {
        Optional<Post> post = postRepository.findById(id);
        post.ifPresent(po -> po.setTags(tagService.getTagsByPost(id)));
        return post;
    }

//    @Override
//    public List<Post> getByTag(String name) {
//        return postRepository.findByTag(name);
//    }

    @Override
    public void create(Post post) {
        post.setCreatedAt(LocalDateTime.now());
        postRepository.save(post);
    }

    @Override
    public void update(Post post) {
        post.setUpdatedAt(LocalDateTime.now());
        postRepository.update(post);
    }

    @Override
    public void delete(Long id) {
        postRepository.delete(id);
    }

    @Override
    public List<PostList> listPosts(int limit, int offset, String filter) {
        return postRepository.findAll(limit, offset, filter);
    }
}
