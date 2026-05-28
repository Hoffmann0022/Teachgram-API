package com.Teach.Teachgram.controller;

import com.Teach.Teachgram.dto.PostDTO.*;
import com.Teach.Teachgram.service.PostService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/posts")
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;

    // POST /api/posts — Criar post
    @PostMapping
    public ResponseEntity<PostResponse> create(@Valid @RequestBody CreateRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(postService.create(request));
    }

    // GET /api/posts/user/{userId} — Listar posts do usuário
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<PostResponse>> getByUser(@PathVariable Long userId) {
        return ResponseEntity.ok(postService.getByUserId(userId));
    }
}