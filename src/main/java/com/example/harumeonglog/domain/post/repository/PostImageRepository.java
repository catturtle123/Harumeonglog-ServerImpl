package com.example.harumeonglog.domain.post.repository;

import com.example.harumeonglog.domain.post.entity.PostImage;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostImageRepository extends JpaRepository<PostImage, Long> {
}
