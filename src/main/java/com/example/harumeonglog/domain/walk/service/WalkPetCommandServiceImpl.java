package com.example.harumeonglog.domain.walk.service;

import com.example.harumeonglog.domain.pet.entity.Pet;
import com.example.harumeonglog.domain.pet.repository.PetRepository;
import com.example.harumeonglog.domain.walk.converter.WalkPetConverter;
import com.example.harumeonglog.domain.walk.entity.Walk;
import com.example.harumeonglog.domain.walk.entity.WalkPet;
import com.example.harumeonglog.domain.walk.repository.WalkPetRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class WalkPetCommandServiceImpl implements WalkPetCommandService {

    private final WalkPetRepository walkPetRepository;

    @Override
    public WalkPet createWalkPet(Walk walk, Pet pet) {
        return walkPetRepository.save(WalkPetConverter.toWalkPet(walk, pet));
    }
}
