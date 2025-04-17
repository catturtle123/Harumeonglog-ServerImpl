package com.example.harumeonglog.domain.pet.service.query;

import com.example.harumeonglog.domain.pet.dto.response.PetResponse;
import org.springframework.stereotype.Service;

@Service
public class PetQueryServiceImpl implements PetQueryService {
    @Override
    public PetResponse.GetPetsResponse getPets(Long cursor, int size) {
        return null;
    }

    @Override
    public PetResponse.SearchMemberResponse searchMember(String email, Long cursor, int size) {
        return null;
    }
}
