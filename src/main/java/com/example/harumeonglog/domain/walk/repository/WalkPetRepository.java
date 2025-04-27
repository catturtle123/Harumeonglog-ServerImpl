package com.example.harumeonglog.domain.walk.repository;

import com.example.harumeonglog.domain.walk.entity.WalkPet;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WalkPetRepository extends JpaRepository<WalkPet, Long> {
}
