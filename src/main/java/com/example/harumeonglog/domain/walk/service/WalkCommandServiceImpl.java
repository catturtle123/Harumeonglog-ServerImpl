package com.example.harumeonglog.domain.walk.service;

import com.example.harumeonglog.domain.event.dto.request.EventRequest;
import com.example.harumeonglog.domain.event.entity.enums.EventCategory;
import com.example.harumeonglog.domain.event.service.command.EventCommandService;
import com.example.harumeonglog.domain.member.entity.Member;
import com.example.harumeonglog.domain.member.repository.MemberRepository;
import com.example.harumeonglog.domain.pet.entity.Pet;
import com.example.harumeonglog.domain.pet.repository.MemberPetRepository;
import com.example.harumeonglog.domain.pet.repository.PetRepository;
import com.example.harumeonglog.domain.walk.converter.WalkConverter;
import com.example.harumeonglog.domain.walk.dto.request.WalkRequest;
import com.example.harumeonglog.domain.walk.dto.response.WalkResponse;
import com.example.harumeonglog.domain.walk.entity.*;
import com.example.harumeonglog.domain.walk.entity.enums.WalkStatus;
import com.example.harumeonglog.domain.walk.repository.WalkLikeRepository;
import com.example.harumeonglog.domain.walk.repository.WalkRepository;
import com.example.harumeonglog.global.error.code.MemberErrorCode;
import com.example.harumeonglog.global.error.code.PetErrorCode;
import com.example.harumeonglog.global.error.code.WalkErrorCode;
import com.example.harumeonglog.global.error.exception.MemberException;
import com.example.harumeonglog.global.error.exception.PetException;
import com.example.harumeonglog.global.error.exception.WalkException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Collection;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class WalkCommandServiceImpl implements WalkCommandService {

    private static final String DEFAULT_TITLE_FORMAT = "%s시 산책";
    private static final String DEFAULT_DETAILS = "%s시 %s분 시작, %s시 %s분 종료";

    private final WalkRepository walkRepository;
    private final PetRepository petRepository;
    private final MemberRepository memberRepository;
    private final MemberPetRepository memberPetRepository;
    private final WalkLikeRepository walkLikeRepository;

    private final WalkQueryService walkQueryService;
    private final TrackQueryService trackQueryService;
    private final WalkPositionQueryService walkPositionQueryService;
    private final MemberWalkQueryService memberWalkQueryService;
    private final WalkPetQueryService walkPetQueryService;

    private final EventCommandService eventCommandService;
    private final TrackCommandService trackCommandService;
    private final WalkPositionCommandService walkPositionCommandService;
    private final MemberWalkCommandService memberWalkCommandService;
    private final WalkPetCommandService walkPetCommandService;
    private final WalkLikeCommandService walkLikeCommandService;

    @Override
    public WalkResponse.WalkStartResponse startWalk(Member member, WalkRequest.WalkStartRequest request) {
        Walk walk = createWalk(request.getStartLatitude(), request.getStartLongitude());
        createWalkPet(member, walk, request.getPetId());
        createMemberWalk(walk, request.getMemberId());
        Track track = startTrack(walk, request.getStartLatitude(), request.getStartLongitude());
        return WalkConverter.toWalkStartResponse(walk, track);
    }

    @Override
    public WalkResponse.WalkPauseResponse pauseWalk(Long walkId) {
        Walk walk = walkQueryService.findById(walkId);

        if (!walkQueryService.hasStatus(walk, WalkStatus.PROCESS)) {
            throw new WalkException(WalkErrorCode.CANNOT_CHANGE_STATUS);
        }

        walk.updateWalkStatus(WalkStatus.PAUSE);
        return WalkConverter.toWalkPauseResponse(walk);
    }

    @Override
    public WalkResponse.WalkResumeResponse resumeWalk(Long walkId, WalkRequest.WalkResumeRequest request) {
        Walk walk = walkQueryService.findById(walkId);

        if (!walkQueryService.hasStatus(walk, WalkStatus.PAUSE)) {
            throw new WalkException(WalkErrorCode.CANNOT_CHANGE_STATUS);
        }

        walk.updateWalkStatus(WalkStatus.PROCESS);
        Track track = startTrack(walk, request.getLatitude(), request.getLongitude());
        return WalkConverter.toWalkResumeResponse(walk, track);
    }

    @Override
    public WalkResponse.WalkEndResponse endWalk(Long walkId, WalkRequest.WalkEndRequest request) {
        Walk walk = walkQueryService.findById(walkId);

        if (!walkQueryService.hasStatus(walk, WalkStatus.PROCESS, WalkStatus.PAUSE)) {
            throw new WalkException(WalkErrorCode.CANNOT_CHANGE_STATUS);
        }

        walk.updateWalkStatus(WalkStatus.DONE);
        walk.updateTime(request.getTime());
        walk.updateDistance(request.getDistance());
        createEventAfterWalk(walk, request);
        return WalkConverter.toWalkEndResponse(walk);
    }

    @Override
    public WalkResponse.PositionCreateResponse addPosition(WalkRequest.PositionRequest request, Long trackId) {
        Track track = trackQueryService.getTrackWithFetchedWalk(trackId);

        if (!walkQueryService.hasStatus(track.getWalk(), WalkStatus.PROCESS)) {
            throw new WalkException(WalkErrorCode.CANNOT_ADD_POSITION);
        }

        WalkPosition walkPosition = addPosition(track, request.getLatitude(), request.getLongitude());
        return WalkConverter.toPositionCreateResponse(track, walkPosition);
    }

    @Override
    public WalkResponse.WalkUpdateResponse updateWalk(Long walkId, WalkRequest.WalkUpdateRequest request) {
        Walk walk = walkQueryService.findById(walkId);

        walk.updateTitle(request.getTitle());
        return WalkConverter.toWalkUpdateResponse(walk);
    }

    @Override
    public WalkResponse.WalkShareResponse shareWalk(Long id) {
        Walk walk = walkQueryService.findById(id);

        walk.invertShare();
        return WalkConverter.toWalkShareResponse(walk);
    }

    @Override
    public WalkResponse.WalkLikeResponse likeWalk(Member member, Long walkId) {
        Walk walk = walkQueryService.findById(walkId);

        processLike(member, walk);
        return WalkConverter.toWalkLikeResponse(walk);
    }

    private Walk createWalk(Double startLatitude, Double startLongitude) {
        return walkRepository.save(WalkConverter.toWalk(startLatitude, startLongitude));
    }

    private Track startTrack(Walk walk, Double latitude, Double longitude) {
        Track track = trackCommandService.createNewTrack(walk);
        createWalkPosition(track, latitude, longitude);
        return track;
    }

    private WalkPosition addPosition(Track track, Double latitude, Double longitude) {
        WalkPosition lastPosition = walkPositionQueryService.getLastPosition(track);

        // 최근 위치와 같은 경우 무시
        if (lastPosition.getLatitude().equals(latitude) && lastPosition.getLongitude().equals(longitude)) {
            return lastPosition;
        }

        return createWalkPosition(track, latitude, longitude);
    }

    private WalkPosition createWalkPosition(Track track, Double latitude, Double longitude) {
        return walkPositionCommandService.createWalkPosition(track, latitude, longitude);
    }

    private void processLike(Member member, Walk walk) {
        walkLikeRepository.findByMemberAndWalk(member, walk).ifPresentOrElse(
                walkLike -> {
                    walkLikeCommandService.deleteWalkLike(walkLike);
                    walk.changeLikeNum(-1L);
                },
                () ->  {
                    walkLikeCommandService.createWalkLike(member, walk);
                    walk.changeLikeNum(1L);
                }
        );
    }

    private void createWalkPet(Member member, Walk walk, Collection<Long> petIds) {
        petIds.forEach(petId -> {
            Pet pet = petRepository.findById(petId).orElseThrow(() ->
                    new PetException(PetErrorCode.NOT_FOUND));
            // 산책 가능 여부 확인
            if (!isAvailableWalkWithPet(member, pet)) {
                throw new WalkException(WalkErrorCode.UNAVAILABLE_WALK);
            }
            walkPetCommandService.createWalkPet(walk, pet);
        });
    }

    private boolean isAvailableWalkWithPet(Member member, Pet pet) {
        return memberPetRepository.existsByMemberAndPet(member ,pet);
    }

    private void createMemberWalk(Walk walk, Collection<Long> memberIds) {
        memberIds.forEach(memberId -> {
            Member foundMember = memberRepository.findById(memberId).orElseThrow(() ->
                    new MemberException(MemberErrorCode.NOT_FOUND));
            memberWalkCommandService.createMemberWalk(foundMember, walk);
        });
    }

    private void createEventAfterWalk(Walk walk, WalkRequest.WalkEndRequest request) {
        EventRequest.EventRequestDTO dto = createEventRequest(request, walk);
        List<Member> members = memberWalkQueryService.findByWalk(walk.getId()).stream().map(MemberWalk::getMember).toList();
        List<Pet> pets = walkPetQueryService.findByWalk(walk.getId()).stream().map(WalkPet::getPet).toList();

        for (Pet pet : pets) {
            for (Member member : members) {
                if (checkOwnPet(member, pet)) {
                    eventCommandService.createEventAfterWalk(dto, member, pet);
                    break;
                }
            }
        }
    }

    private EventRequest.EventRequestDTO createEventRequest(WalkRequest.WalkEndRequest request, Walk walk) {
        LocalDate today = LocalDate.now();
        LocalTime time = LocalTime.now();
        return EventRequest.EventRequestDTO.builder()
                .title(String.format(DEFAULT_TITLE_FORMAT, walk.getCreatedAt().getHour()))
                .date(today)
                .isRepeated(false)
                .expiredDate(null)
                .repeatDays(null)
                .hasNotice(false)
                .time(time)
                .category(EventCategory.WALK)
                .details(String.format(DEFAULT_DETAILS, walk.getCreatedAt().getHour(), walk.getCreatedAt().getMinute(), time.getHour(), time.getMinute()))
                .distance(String.valueOf(request.getDistance()))
                .duration(String.valueOf(request.getTime()))
                .build();
    }

    private boolean checkOwnPet(Member member, Pet pet) {
        return memberPetRepository.existsByMemberAndPet(member, pet);
    }
}
