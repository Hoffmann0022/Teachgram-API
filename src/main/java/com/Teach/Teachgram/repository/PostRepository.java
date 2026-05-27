package com.Teach.Teachgram.repository;

import com.Teach.Teachgram.model.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {
    List<Post> findByUserIdAndDeletedFalse(Long userId);
}