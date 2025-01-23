package com.basebook.web.controller;

import com.basebook.model.Post;
import com.basebook.model.PostList;
import com.basebook.model.Tag;
import com.basebook.service.ImageService;
import com.basebook.service.PostService;
import com.basebook.service.TagService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Controller
@RequestMapping("/posts")
@Slf4j
public class PostController {

    public static final String LISTING_POST_WITH_POSTS_TAGS_PAGENUM_SIZE =
            "Listing post with parameters posts {} tags {} selected tag {} pagenum {} size {}";
    private final PostService postService;
    private final TagService tagService;
    private final ImageService imageService;

    public PostController(PostService postService, TagService tagService, ImageService imageService) {
        this.postService = postService;
        this.tagService = tagService;
        this.imageService = imageService;
    }

    @GetMapping
    public String listPosts(
            @RequestParam(value = "size", defaultValue = "10") int size,
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "tag", defaultValue = "-", required = false) String tag,
            Model model
    ) {
        int offset = page * size;
        List<PostList> posts = postService.listPosts(size, offset, tag);

        List<Tag> allTags = tagService.getAll();

        model.addAttribute("posts", posts);
        model.addAttribute("tags", allTags);
        model.addAttribute("selectedTag", tag);
        model.addAttribute("currentPage", page);
        model.addAttribute("pageSize", size);
        log.info(LISTING_POST_WITH_POSTS_TAGS_PAGENUM_SIZE, posts.toArray(), allTags.toArray(), tag, page, size);
        return "post_list";
    }

    @GetMapping("/{id}")
    public String viewPost(@PathVariable("id") Long id,
                           Model model) {
        log.info("View post: {}", id);
        return postService.get(id).map(post -> {
            model.addAttribute("post", post);
            return "post_detail";
        }).orElse("redirect:/posts");
    }

    @PostMapping
    public String createPost(@ModelAttribute Post post,
                             @RequestParam(value = "image", required = false) MultipartFile imageFile) {
        log.info("Start post creation: {}", post);
        if (!imageFile.isEmpty()) {
            try {
                post.setImageUrl(imageService
                        .saveImage(imageFile.getOriginalFilename(), imageFile.getBytes()));
                log.info("Image saved {}", post.getImageUrl());
            } catch (IOException e) {
                throw new RuntimeException("Failed to save image", e);
            }
        }
        postService.create(post);
        return "redirect:/posts";
    }

    @PostMapping("/edit/")
    public String edit(@ModelAttribute Post post,
                       @RequestParam("image") MultipartFile imageFile) {
        if (!imageFile.isEmpty()) {
            try {
                post.setImageUrl(imageService
                        .saveImage(imageFile.getOriginalFilename(), imageFile.getBytes()));
                log.info("Image updated {}", post.getImageUrl());
            } catch (IOException e) {
                throw new RuntimeException("Failed to save image", e);
            }
        }
        postService.update(post);
        return "redirect:/posts/" + post.getId();
    }

    @PostMapping("{id}/delete")
    public String delete(@PathVariable("id") Long id) {
        return postService.get(id).map(post -> {
            postService.delete(post.getId());
            log.info("Correct delete {}", id);
            return "redirect:/posts";
        }).orElseGet(() -> {
            log.info("Invalid delete {}", id);
            return "redirect:/posts";
        });
    }

}
