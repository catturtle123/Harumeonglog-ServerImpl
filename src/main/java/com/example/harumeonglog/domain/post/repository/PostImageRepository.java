package com.example.harumeonglog.domain.post.repository;

import com.example.harumeonglog.domain.post.entity.Post;
import com.example.harumeonglog.domain.post.entity.PostImage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface PostImageRepository extends JpaRepository<PostImage, Long> {
    @Query("SELECT pi.postImageKeyName FROM PostImage pi")
    List<String> findAllImageKeys();

    List<PostImage> findAllByPost(Post post);
}
