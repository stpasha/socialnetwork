package com.basebook.web.controller;

import com.basebook.model.Comment;
import com.basebook.service.CommentService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/comments")
@Slf4j
public class CommentController {

    private final CommentService commentService;

    public CommentController(CommentService commentService) {
        this.commentService = commentService;
    }

    @PostMapping("/{postId}")
    public String addComment(
            @PathVariable("postId") Long postId,
            @RequestParam("content") String content
    ) {
        log.info("Add a comment to postId: {}", postId);
        commentService.create(Comment.builder().postId(postId).content(content).build());
        return "redirect:/posts/" + postId;
    }

    @PostMapping("/{id}/delete")
    public String deleteComment(
            @PathVariable("id") Long commentId,
            @RequestParam("postId") Long postId
    ) {
        log.info("Delete Comment id {} postId: {}", commentId, postId);
        commentService.delete(commentId);
        return "redirect:/posts/" + postId;
    }

    @PostMapping("/{id}/edit")
    public String editComment(
            @PathVariable("id") Long commentId,
            @ModelAttribute Comment comment
    ) {
        log.info("Update {}", comment);
        commentService.update(comment);
        return "redirect:/posts/" + comment.getPostId();
    }
}