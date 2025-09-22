package com.example.harumeonglog.domain.member.entity;

import com.example.harumeonglog.domain.comment.entity.Comment;
import com.example.harumeonglog.global.common.entity.BaseEntity;
import com.example.harumeonglog.domain.member.entity.enums.SocialType;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Table(name = "member")
public class Member extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_id")
    private Long id;

    @Column(name = "email", nullable = false)
    private String email;

    @Column(name = "nickname", nullable = false)
    private String nickname;

    @Column(name = "social_type", nullable = false)
    @Enumerated(EnumType.STRING)
    private SocialType socialType;

    @Column(name = "image")
    private String image;

    @Column(name = "provider_id", nullable = false)
    private String providerId;

    @Column(name = "deleted_at")
    private LocalDateTime deletedAt;

    @Column(name = "current_pet_id")
    private Long currentPetId;

    @Column(name = "terms")
    @Builder.Default
    private Boolean terms = false;

    @OneToMany(mappedBy = "reporter", fetch = FetchType.EAGER)
    private List<MemberBlock> memberBlockList = new ArrayList<>();

    private String deviceId;

    public void update(String nickname, String image) {
         this.nickname = nickname;
         this.image = image;
    }

    public void updateCurrentPetId(Long currentPetId) {
        this.currentPetId = currentPetId;
    }
    public void updateTerms(Boolean terms) {
        this.terms = terms;
    }
    public void softDelete() {
        this.deletedAt = LocalDateTime.now();
    }

    public void cancelSoftDelete() {
        this.deletedAt = null;
    }

    public void updateFCMToken(String fcmToken) {
        this.deviceId = fcmToken;
    }
}
