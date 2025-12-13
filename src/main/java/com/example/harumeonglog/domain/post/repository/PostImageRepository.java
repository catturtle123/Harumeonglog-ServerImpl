package com.example.harumeonglog.domain.post.repository;

import com.example.harumeonglog.domain.post.entity.Post;
import com.example.harumeonglog.domain.post.entity.PostImage;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Set;

public interface PostImageRepository extends JpaRepository<PostImage, Long> {
    @Query("SELECT pi.postImageKeyName FROM PostImage pi")
    List<String> findAllImageKeys();

    List<PostImage> findAllByPost(Post post);


    @Modifying
    @Query("DELETE FROM PostImage p WHERE p.postImageKeyName IN :imageKeys")
    int deleteByImageKeyIn(@Param("imageKeys") Set<String> imageKeys);
}
