package com.example.harumeonglog.domain.post.repository;

import com.example.harumeonglog.domain.post.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostRepository extends JpaRepository<Post, Long> {
}
