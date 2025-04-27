package com.example.harumeonglog.domain.walk.converter;

import com.example.harumeonglog.domain.pet.entity.Pet;
import com.example.harumeonglog.domain.walk.entity.Walk;
import com.example.harumeonglog.domain.walk.entity.WalkPet;

public class WalkPetConverter {
    public static WalkPet toWalkPet(Walk walk, Pet pet) {
        return WalkPet.builder()
                .walk(walk)
                .pet(pet)
                .build();
    }
}
