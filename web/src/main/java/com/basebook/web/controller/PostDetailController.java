package com.basebook.web.controller;

import com.basebook.model.Post;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/posts")
public class PostDetailController {
    @GetMapping("/{id}")
    public String viewPost(@PathVariable Long id, Model model) {
        // Логика для отображения страницы поста
        return "post_detail";
    }

    @PostMapping
    public String addPost(@ModelAttribute Post post) {
        return "post_detail";
    }
}
