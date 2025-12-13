package com.example.harumeonglog.domain.post.controller.enums;

import com.example.harumeonglog.domain.post.entity.enums.PostCategory;

public enum PostRequestCategory {
    INFO, // 정보 공유
    HUMOR, // 유머
    QNA, // 질문
    SNS, // SNS
    ETC, // 기타
    ALL // 전부
    ;

    public boolean isAll() {
        return ALL.equals(this);
    }

    public PostCategory toPostCategory() {
        return PostCategory.valueOf(this.name());
    }
}
