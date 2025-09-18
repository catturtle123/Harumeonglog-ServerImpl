package com.example.harumeonglog.domain.pet.converter;

import com.example.harumeonglog.domain.member.entity.Member;
import com.example.harumeonglog.domain.member.entity.enums.MemberPetRole;
import com.example.harumeonglog.domain.pet.dto.response.PetResponse;
import com.example.harumeonglog.domain.pet.entity.MemberPet;
import com.example.harumeonglog.domain.pet.entity.Pet;
import com.example.harumeonglog.global.util.S3Util;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Slice;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
public class MemberPetConverter {

    public static MemberPet toMemberPet( Member member, Pet pet, MemberPetRole role){
        return MemberPet.builder()
                .pet(pet)
                .member(member)
                .role(role)
                .build();
    }
    public static PetResponse.GetPetsResponse toGetPetsResponse(
            Slice<MemberPet> memberPetSlice,
            List<MemberPet> currentMemberPets,
            List<MemberPet> relatedMemberPets,
            S3Util s3Util) {
        List<PetResponse.PetInfo> petInfos = currentMemberPets.stream()
                .map(mp -> toPetInfoResponse(mp, relatedMemberPets, s3Util))
                .collect(Collectors.toList());

        Long nextCursor = memberPetSlice.hasNext() ?
                currentMemberPets.get(currentMemberPets.size() - 1).getPet().getId() : null;

        return PetResponse.GetPetsResponse.builder()
                .pets(petInfos)
                .cursor(nextCursor)
                .hasNext(memberPetSlice.hasNext())
                .build();
    }

    public static PetResponse.PetInfo toPetInfoResponse(
            MemberPet memberPet,
            List<MemberPet> relatedMemberPets,
            S3Util s3Util) {
        Pet pet = memberPet.getPet();
        PetResponse.PeopleInfo currentPeopleInfo = PetResponse.PeopleInfo.builder()
                .id(memberPet.getMember().getId())
                .name(memberPet.getMember().getNickname())
                .role(memberPet.getRole().name())
                .image(memberPet.getMember().getImage() != null ?
                        s3Util.getUrlFromKey(memberPet.getMember().getImage()) : null)
                .build();

        List<PetResponse.PeopleInfo> relatedPeopleInfos = relatedMemberPets.stream()
                .filter(mp -> mp.getPet().getId().equals(pet.getId()))
                .map(mp -> PetResponse.PeopleInfo.builder()
                        .id(mp.getMember().getId())
                        .name(mp.getMember().getNickname())
                        .role(mp.getRole().name())
                        .image(mp.getMember().getImage() != null ?
                                s3Util.getUrlFromKey(mp.getMember().getImage()) : null)
                        .build())
                .toList();

        List<PetResponse.PeopleInfo> peopleInfos = new ArrayList<>();
        peopleInfos.add(currentPeopleInfo);
        peopleInfos.addAll(relatedPeopleInfos);

        return PetResponse.PetInfo.builder()
                .role(memberPet.getRole().name())
                .petId(pet.getId())
                .name(pet.getName())
                .size(pet.getSize())
                .type(pet.getType())
                .gender(pet.getGender())
                .birth(pet.getBirth())
                .mainImage(pet.getMainImage() != null ?
                        s3Util.getUrlFromKey(pet.getMainImage()) : null)
                .people(peopleInfos)
                .build();
    }

    public static PetResponse.PetListPreviewResponse toGetChangePetResponse(
            Slice<MemberPet> memberPetSlice, S3Util s3Util) {
        List<MemberPet> memberPets = memberPetSlice.getContent();
        List<PetResponse.PetPreviewResponse> petPreviewResponse = memberPets.stream()
                .map(mp -> PetResponse.PetPreviewResponse.builder()
                        .petId(mp.getPet().getId())
                        .name(mp.getPet().getName())
                        .mainImage(mp.getPet().getMainImage() != null ?
                                s3Util.getUrlFromKey(mp.getPet().getMainImage()) : null)
                        .role(mp.getRole().name())
                        .build())
                .collect(Collectors.toList());

        Long nextCursor = memberPetSlice.hasNext() ?
                memberPets.get(memberPets.size() - 1).getPet().getId() : null;

        return PetResponse.PetListPreviewResponse.builder()
                .pets(petPreviewResponse)
                .cursor(nextCursor)
                .hasNext(memberPetSlice.hasNext())
                .build();
    }

    public static PetResponse.SearchMemberResponse toSearchMemberResponse(
            Slice<Member> memberSlice, S3Util s3Util) {
        List<PetResponse.SearchMemberResponse.MemberInfo> memberInfos = memberSlice.getContent().stream()
                .map(member -> toMemberInfo(member, s3Util))
                .collect(Collectors.toList());

        Long nextCursor = memberSlice.hasNext() ?
                memberInfos.get(memberInfos.size() - 1).getMemberId() : null;

        return PetResponse.SearchMemberResponse.builder()
                .members(memberInfos)
                .cursor(nextCursor)
                .hasNext(memberSlice.hasNext())
                .build();
    }

    public static PetResponse.SearchMemberResponse.MemberInfo toMemberInfo(Member member, S3Util s3Util) {
        return PetResponse.SearchMemberResponse.MemberInfo.builder()
                .memberId(member.getId())
                .email(member.getEmail())
                .name(member.getNickname())
                .image(member.getImage() != null ?
                        s3Util.getUrlFromKey(member.getImage()) : null)
                .build();
    }
}
