package com.example.harumeonglog.domain.post.converter;

import com.example.harumeonglog.domain.post.entity.PostImage;

public class PostImageConverter {

    public static PostImage toPostImage(String imageKeyName) {
        return PostImage.builder()
                .postImageKeyName(imageKeyName)
                .build();
    }
}
