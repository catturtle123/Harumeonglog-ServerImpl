package com.example.harumeonglog.domain.walk.converter;

import com.example.harumeonglog.domain.member.entity.Member;
import com.example.harumeonglog.domain.pet.entity.Pet;
import com.example.harumeonglog.domain.walk.dto.response.WalkResponse;
import com.example.harumeonglog.domain.walk.entity.Track;
import com.example.harumeonglog.domain.walk.entity.Walk;
import com.example.harumeonglog.domain.walk.entity.WalkPosition;
import com.example.harumeonglog.domain.walk.entity.enums.WalkStatus;
import com.example.harumeonglog.global.util.DistanceUtil;
import com.example.harumeonglog.global.util.S3Util;

import java.util.Collection;
import java.util.List;

public class WalkConverter {

    public static Walk toWalk(double startLatitude, double startLongitude) {
        return Walk.builder()
                .distance(0.0)
                .time(0L)
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

    public static WalkResponse.WalkAvailablePetListResponse toWalkAvailablePetListResponse(List<Pet> pets, S3Util s3Util) {
        return WalkResponse.WalkAvailablePetListResponse.builder()
                .pets(pets.stream().map(pet -> toWalkAvailablePetInfoResponse(pet, s3Util)).toList())
                .size(pets.size())
                .build();
    }

    public static WalkResponse.WalkAvailablePetInfoResponse toWalkAvailablePetInfoResponse(Pet pet, S3Util s3Util) {
        return WalkResponse.WalkAvailablePetInfoResponse.builder()
                .petId(pet.getId())
                .name(pet.getName())
                .image(pet.getMainImage() == null ? null : s3Util.getUrlFromKey(pet.getMainImage()))
                .build();
    }

    public static WalkResponse.WalkAvailableMemberListResponse toWalkAvailableMemberListResponse(Collection<Member> members, S3Util s3Util) {
        return WalkResponse.WalkAvailableMemberListResponse.builder()
                .members(members.stream().map(member -> toWalkAvailableMemberResponse(member, s3Util)).toList())
                .size(members.size())
                .build();
    }

    public static WalkResponse.WalkAvailableMemberResponse toWalkAvailableMemberResponse(Member member, S3Util s3Util) {
            return WalkResponse.WalkAvailableMemberResponse.builder()
                    .memberId(member.getId())
                    .nickname(member.getNickname())
                    .image(member.getImage() == null ? null : s3Util.getUrlFromKey(member.getImage()))
                    .build();
    }

    public static WalkResponse.WalkSearchListResponse toWalkSearchListResponse(List<WalkResponse.WalkSearchResponse> items, Long cursor, boolean hasNext) {
        return WalkResponse.WalkSearchListResponse.builder()
                .items(items)
                .hasNext(hasNext)
                .cursor(cursor)
                .build();
    }

    public static WalkResponse.WalkSearchResponse toWalkSearchResponse(Walk walk, String nickname, Boolean isLike) {
        return WalkResponse.WalkSearchResponse.builder()
                .id(walk.getId())
                .title(walk.getTitle())
                .time(walk.getTime())
                .distance(walk.getDistance())
                .walkLikeNum(walk.getWalkLikeNum())
                .isLike(isLike)
                .memberNickname(nickname)
                .build();
    }

    public static WalkResponse.WalkDetailResponse toWalkDetailResponse(Walk walk, String nickname, Boolean isLike) {
        return WalkResponse.WalkDetailResponse.builder()
                .id(walk.getId())
                .title(walk.getTitle())
                .walkLikeNum(walk.getWalkLikeNum())
                .distance(walk.getDistance())
                .time(walk.getTime())
                .memberNickname(nickname)
                .isLike(isLike)
                .tracks(walk.getTrackList().stream().map(WalkConverter::toWalkResponseTrack).toList())
                .build();

    }

    public static WalkResponse.Track toWalkResponseTrack(Track track) {
        return WalkResponse.Track.builder()
                .trackId(track.getId())
                .positions(track.getWalkPositionList().stream().map(WalkConverter::toWalkResponsePosition).toList())
                .build();
    }

    public static WalkResponse.Position toWalkResponsePosition(WalkPosition walkPosition) {
        return WalkResponse.Position.builder()
                .latitude(walkPosition.getLatitude())
                .longitude(walkPosition.getLongitude())
                .build();
    }

    public static WalkResponse.PositionCreateResponse toPositionCreateResponse(Track track, WalkPosition walkPosition) {
        return WalkResponse.PositionCreateResponse.builder()
                .trackId(track.getId())
                .positionId(walkPosition.getId())
                .createdAt(walkPosition.getCreatedAt())
                .updatedAt(walkPosition.getUpdatedAt())
                .build();
    }

    public static WalkResponse.WalkUpdateResponse toWalkUpdateResponse(Walk walk) {
        return WalkResponse.WalkUpdateResponse.builder()
                .walkId(walk.getId())
                .title(walk.getTitle())
                .updatedAt(walk.getUpdatedAt())
                .build();
    }
}
