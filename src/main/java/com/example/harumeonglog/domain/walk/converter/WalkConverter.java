package com.example.harumeonglog.domain.walk.converter;

import com.example.harumeonglog.domain.member.entity.Member;
import com.example.harumeonglog.domain.pet.entity.Pet;
import com.example.harumeonglog.domain.walk.dto.response.WalkResponse;
import com.example.harumeonglog.domain.walk.entity.Track;
import com.example.harumeonglog.domain.walk.entity.Walk;
import com.example.harumeonglog.domain.walk.entity.enums.WalkStatus;

import java.util.Collection;
import java.util.List;

public class WalkConverter {

    public static Walk toWalk(double startLatitude, double startLongitude) {
        return Walk.builder()
                .distance(0.0)
                .time(0)
                .startLatitude(startLatitude)
                .startLongitude(startLongitude)
                .walkLikeNum(0L)
                .isShared(false)
                .status(WalkStatus.PROCESS)
                .build();
    }

    public static WalkResponse.WalkStartResponse toWalkStartResponse(Walk walk, Track track) {
        return WalkResponse.WalkStartResponse.builder()
                .walkId(walk.getId())
                .trackId(track.getId())
                .createdAt(walk.getCreatedAt())
                .updatedAt(walk.getUpdatedAt())
                .build();
    }

    public static WalkResponse.WalkPauseResponse toWalkPauseResponse(Walk walk) {
        return WalkResponse.WalkPauseResponse.builder()
                .walkId(walk.getId())
                .status(walk.getStatus().name())
                .updatedAt(walk.getUpdatedAt())
                .build();
    }

    public static WalkResponse.WalkResumeResponse toWalkResumeResponse(Walk walk, Track track) {
        return WalkResponse.WalkResumeResponse.builder()
                .walkId(walk.getId())
                .trackId(track.getId())
                .status(walk.getStatus().name())
                .updatedAt(walk.getUpdatedAt())
                .build();
    }

    public static WalkResponse.WalkEndResponse toWalkEndResponse(Walk walk) {
        return WalkResponse.WalkEndResponse.builder()
                .walkId(walk.getId())
                .updatedAt(walk.getUpdatedAt())
                .build();
    }

    public static WalkResponse.WalkShareResponse toWalkShareResponse(Walk walk) {
        return WalkResponse.WalkShareResponse.builder()
                .walkId(walk.getId())
                .isShared(walk.getIsShared())
                .updatedAt(walk.getUpdatedAt())
                .build();
    }

    public static WalkResponse.WalkLikeResponse toWalkLikeResponse(Walk walk) {
        return WalkResponse.WalkLikeResponse.builder()
                .walkId(walk.getId())
                .walkLikeNum(walk.getWalkLikeNum())
                .updatedAt(walk.getUpdatedAt())
                .build();
    }

    public static WalkResponse.WalkAvailablePetListResponse toWalkAvailablePetListResponse(List<Pet> pets) {
        return WalkResponse.WalkAvailablePetListResponse.builder()
                .pets(pets.stream().map(WalkConverter::toWalkAvailablePetInfoResponse).toList())
                .size(pets.size())
                .build();
    }

    public static WalkResponse.WalkAvailablePetInfoResponse toWalkAvailablePetInfoResponse(Pet pet) {
        return WalkResponse.WalkAvailablePetInfoResponse.builder()
                .petId(pet.getId())
                .name(pet.getName())
                .image(pet.getMainImage())
                .build();
    }

    public static WalkResponse.WalkAvailableMemberListResponse toWalkAvailableMemberListResponse(Collection<Member> members) {
        return WalkResponse.WalkAvailableMemberListResponse.builder()
                .members(members.stream().map(WalkConverter::toWalkAvailableMemberResponse).toList())
                .size(members.size())
                .build();
    }

    public static WalkResponse.WalkAvailableMemberResponse toWalkAvailableMemberResponse(Member member) {
            return WalkResponse.WalkAvailableMemberResponse.builder()
                    .memberId(member.getId())
                    .nickname(member.getNickname())
                    .image(member.getImage())
                    .build();
    }

    public static WalkResponse.WalkSearchListResponse toWalkSearchListResponse(List<WalkResponse.WalkSearchResponse> items, Long cursor, boolean hasNext) {
        return WalkResponse.WalkSearchListResponse.builder()
                .items(items)
                .hasNext(hasNext)
                .cursor(cursor)
                .build();
    }
}
