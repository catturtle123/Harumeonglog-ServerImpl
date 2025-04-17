package com.example.harumeonglog.domain.pet.repository;

import com.example.harumeonglog.domain.pet.entity.MemberPet;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberPetRepository extends JpaRepository<MemberPet, Long> {
}
