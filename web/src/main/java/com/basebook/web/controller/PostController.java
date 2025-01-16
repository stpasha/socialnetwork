package com.basebook.web.controller;

import com.basebook.model.Post;
import com.basebook.model.Tag;
import com.basebook.service.PostService;
import com.basebook.service.TagService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/posts")
@Slf4j
public class PostController {

    private final PostService postService;
    private final TagService tagService;

    public PostController(PostService postService, TagService tagService) {
        this.postService = postService;
        this.tagService = tagService;
    }

    @GetMapping
    public String listPosts(
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(required = false) String tag,
            Model model
    ) {
        int offset = page * size;
        List<Post> posts = tag == null
                ? postService.listPosts(size, offset)
                : postService.getByTag(tag);

        List<Tag> allTags = tagService.getAll();

        model.addAttribute("posts", posts);
        model.addAttribute("tags", allTags);
        model.addAttribute("selectedTag", tag);
        model.addAttribute("currentPage", page);
        model.addAttribute("pageSize", size);

        return "post_list";
    }

    @GetMapping("/{id}")
    public String viewPost(@PathVariable Long id, Model model) {
        Post post = postService.get(id);
        if (post == null) {
            return "redirect:/posts";
        }

        model.addAttribute("post", post);
        return "post_detail";
    }

//    @GetMapping("/new")
//    public String newPostForm(Model model) {
//        model.addAttribute("post", new Post());
//        model.addAttribute("tags", tagService.getAll());
//        return "post_form";
//    }

    @PostMapping
    public String createPost(@ModelAttribute Post post) {
        postService.create(post);
        return "redirect:/posts";
    }

    @PostMapping("/{id}/like")
    public String likePost(@PathVariable Long id) {
        //TODO Likes logic
        return "redirect:/posts/" + id;
    }

    @PostMapping("/{id}/comment")
    public String addComment(@PathVariable Long id, @RequestParam String content) {
        //TODO Add comment logic
        return "redirect:/posts/" + id;
    }
}
