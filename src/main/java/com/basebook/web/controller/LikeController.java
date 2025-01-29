package com.basebook.web.controller;

import com.basebook.model.Like;
import com.basebook.service.LikeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.time.LocalDateTime;

import static org.springframework.http.HttpStatus.OK;

@RequestMapping("/posts")
@Slf4j
@Controller
public class LikeController {

    private final LikeService likeService;

    public LikeController(LikeService likeService) {
        this.likeService = likeService;
    }

    @ResponseBody
    @PostMapping("{id}/like")
    public ResponseEntity<Long> like(@PathVariable("id") Long id) {
        log.info("Add a like to postId: {}", id);
        likeService.create(Like.builder().postId(id).createdAt(LocalDateTime.now()).build());
        return new ResponseEntity<>(likeService.countLikesByPost(id), OK);
    }
}
