package com.example.harumeonglog.domain.walk.repository;

import com.example.harumeonglog.domain.walk.entity.WalkPet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface WalkPetRepository extends JpaRepository<WalkPet, Long> {
    @Query("SELECT wp FROM WalkPet wp JOIN FETCH wp.pet p WHERE wp.walk.id = :walkId")
    List<WalkPet> findByWalk(@Param("walkId") Long walkId);
}
