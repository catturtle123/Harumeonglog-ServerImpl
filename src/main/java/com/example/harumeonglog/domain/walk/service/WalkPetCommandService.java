package com.example.harumeonglog.domain.walk.service;

import com.example.harumeonglog.domain.pet.entity.Pet;
import com.example.harumeonglog.domain.walk.entity.Walk;
import com.example.harumeonglog.domain.walk.entity.WalkPet;

public interface WalkPetCommandService {
    WalkPet createWalkPet(Walk walk, Pet pet);
}
