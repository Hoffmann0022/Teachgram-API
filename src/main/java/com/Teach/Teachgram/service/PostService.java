package com.Teach.Teachgram.service;

import com.Teach.Teachgram.model.Post;
import com.Teach.Teachgram.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;

    public List<Post> getPostsByUserId(Long userId) {
        return postRepository.findByUserIdAndDeletedFalse(userId);
    }
}