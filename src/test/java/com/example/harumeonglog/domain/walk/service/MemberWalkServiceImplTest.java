package com.example.harumeonglog.domain.walk.service;

import com.example.harumeonglog.domain.member.domain.Member;
import com.example.harumeonglog.domain.member.domain.enums.SocialType;
import com.example.harumeonglog.domain.pet.domain.Pet;
import com.example.harumeonglog.domain.walk.controller.dto.request.MemberWalkRequest;
import com.example.harumeonglog.domain.walk.controller.port.MemberWalkService;
import com.example.harumeonglog.domain.walk.domain.MemberWalk;
import com.example.harumeonglog.domain.walk.domain.WalkPet;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class MemberWalkServiceImplTest {

    private MemberWalkService memberWalkService;

    @BeforeEach
    void init() {
        memberWalkService = new MemberWalkServiceImpl();
    }

    @Test
    @DisplayName("산책 가능한 반려견 가져오기")
    void getPets() {
        // given
        Member member = Member.builder()
                .email("test@email.com")
                .birth(LocalDate.now())
                .image("https://www.naver.com")
                .providerId("id")
                .socialType(SocialType.KAKAO)
                .createdAt(LocalDateTime.now().minusDays(1))
                .updatedAt(LocalDateTime.now())
                .deletedAt(null)
                .build();
        WalkPet walkPet = new WalkPet();

        // when
        List<Pet> pets = memberWalkService.getPets(member);

        // then
        assertThat(pets).isEmpty();
    }

    @Test
    @DisplayName("산책 가능한 사용자 가져오기")
    void getMembers() {
        // given
        MemberWalkRequest.PetMemberRequest dto = new MemberWalkRequest.PetMemberRequest(List.of(
                1L, 2L
        ));
        MemberWalk memberWalk = new MemberWalk();

        // when
        List<Member> members = memberWalkService.getMembers(dto);

        // then
        assertThat(members).isEmpty();
    }
}