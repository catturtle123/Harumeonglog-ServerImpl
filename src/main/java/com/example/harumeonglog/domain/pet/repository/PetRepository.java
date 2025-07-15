package com.example.harumeonglog.domain.pet.repository;

import com.example.harumeonglog.domain.pet.entity.Pet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface PetRepository extends JpaRepository<Pet, Long> {
    @Query("SELECT p.mainImage FROM Pet p WHERE p.mainImage IS NOT NULL")
    List<String> findAllImageKeys();

    Optional<Pet> findByIdAndDeletedAtIsNull(Long petId);
}
