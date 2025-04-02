package com.example.harumeonglog.domain.member.entity;

import com.example.harumeonglog.domain.member.domain.Member;
import com.example.harumeonglog.domain.common.entity.BaseEntity;
import com.example.harumeonglog.domain.member.domain.enums.SocialType;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Table(name = "member")
public class MemberEntity extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_id")
    private Long id;

    @Column(name = "email", nullable = false)
    private String email;

    @Column(name = "nickname", nullable = false)
    private String nickname;

    @Column(name = "birth")
    private LocalDate birth;

    @Column(name = "social_type", nullable = false)
    @Enumerated(EnumType.STRING)
    private SocialType socialType;

    @Column(name = "image")
    private String image;

    @Column(name = "provider_id", nullable = false)
    private String providerId;

    @Column(name = "deleted_at")
    private LocalDateTime deletedAt;

    public Member toModel() {
        return Member.builder()
                .id(this.id)
                .email(this.email)
                .nickname(this.nickname)
                .birth(this.birth)
                .socialType(this.socialType)
                .image(this.image)
                .providerId(this.providerId)
                .deletedAt(this.deletedAt)
                .build();
    }
}
