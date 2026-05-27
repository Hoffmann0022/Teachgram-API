package com.Teach.Teachgram.controller;

import com.Teach.Teachgram.model.Post;
import com.Teach.Teachgram.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/posts")
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;

    // GET /api/posts/user/{userId}
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Post>> getByUser(@PathVariable Long userId) {
        return ResponseEntity.ok(postService.getPostsByUserId(userId));
    }
}