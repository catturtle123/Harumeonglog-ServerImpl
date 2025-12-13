package com.example.harumeonglog.domain.member.repository;

import com.example.harumeonglog.domain.member.entity.Invitation;
import com.example.harumeonglog.domain.member.entity.Member;
import com.example.harumeonglog.domain.pet.entity.Pet;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface InvitationRepository extends JpaRepository<Invitation, Long> {
    Optional<Invitation> findByPetAndReceiver(Pet pet, Member invitedMember);
    Slice<Invitation> findByReceiverAndIdLessThanOrderByIdDesc(Member invitedMember, Long cursor, Pageable pageable);
}

