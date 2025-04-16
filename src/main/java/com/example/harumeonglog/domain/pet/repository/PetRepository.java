package com.example.harumeonglog.domain.pet.repository;

import com.example.harumeonglog.domain.pet.entity.Pet;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PetRepository extends JpaRepository<Pet, Long> {
}
