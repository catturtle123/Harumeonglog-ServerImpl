package com.example.harumeonglog.domain.post.converter;

import com.example.harumeonglog.domain.post.entity.PostImage;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class PostImageConverter {

    public static PostImage toPostImage(String imageKeyName) {
        return PostImage.builder()
                .postImageKeyName(imageKeyName)
                .build();
    }
}
