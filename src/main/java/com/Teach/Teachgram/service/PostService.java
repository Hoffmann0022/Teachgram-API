package com.Teach.Teachgram.service;

import com.Teach.Teachgram.dto.PostDTO.*;
import com.Teach.Teachgram.exception.ResourceNotFoundException;
import com.Teach.Teachgram.model.Post;
import com.Teach.Teachgram.model.User;
import com.Teach.Teachgram.repository.PostRepository;
import com.Teach.Teachgram.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;
    private final UserRepository userRepository;

    @Transactional
    public PostResponse create(CreateRequest request) {
        User user = userRepository.findById(request.userId())
                .filter(u -> !u.getDeleted())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Usuário com id " + request.userId() + " não encontrado."));

        Post post = Post.builder()
                .title(request.title())
                .description(request.description())
                .photoLink(request.photoLink())
                .videoLink(request.videoLink())
                .isPrivate(request.isPrivate())
                .likes(0)
                .deleted(false)
                .user(user)
                .build();

        return toResponse(postRepository.save(post));
    }

    @Transactional(readOnly = true)
    public List<PostResponse> getByUserId(Long userId) {
        return postRepository.findByUserIdAndDeletedFalse(userId)
                .stream()
                .map(this::toResponse)
                .toList();
    }

    private PostResponse toResponse(Post post) {
        return new PostResponse(
                post.getId(),
                post.getTitle(),
                post.getDescription(),
                post.getPhotoLink(),
                post.getVideoLink(),
                post.getIsPrivate(),
                post.getLikes(),
                post.getDeleted(),
                post.getCreatedAt() != null ? post.getCreatedAt().toString() : null,
                post.getUpdatedAt() != null ? post.getUpdatedAt().toString() : null,
                post.getUser().getId(),
                post.getUser().getUserName()
        );
    }
}