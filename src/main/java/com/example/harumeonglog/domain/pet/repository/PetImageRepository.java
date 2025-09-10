package com.example.harumeonglog.domain.pet.repository;

import com.example.harumeonglog.domain.pet.entity.PetImage;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Set;


public interface PetImageRepository extends JpaRepository<PetImage, Long> {
    @Query("""
        SELECT pi
        FROM PetImage pi
        WHERE pi.pet.id = :petId
        ORDER BY pi.id ASC
    """)
    Slice<PetImage> findByPetId(@Param("petId") Long petId, Pageable pageable);

    @Query("""
        SELECT pi
        FROM PetImage pi
        WHERE pi.pet.id = :petId
        AND pi.id > :cursor
        ORDER BY pi.id ASC
    """)
    Slice<PetImage> findByPetIdAndCursor(
            @Param("petId") Long petId,
            @Param("cursor") Long cursor,
            Pageable pageable
    );

    List<PetImage> findByIdInAndPetId(List<Long> imageIds, Long petId);

    void deleteAllByIdIn(List<Long> imageIds);

    @Query("SELECT pi.imageKey FROM PetImage pi")
    List<String> findAllImageKeys();



    @Modifying
    @Query("DELETE FROM PetImage p WHERE p.imageKey IN :imageKeys")
    int deleteByImageKeyIn(@Param("imageKeys") Set<String> imageKeys);
}
