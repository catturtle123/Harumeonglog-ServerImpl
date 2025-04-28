package com.example.harumeonglog.domain.walk.repository;

import com.example.harumeonglog.domain.walk.entity.Track;
import com.example.harumeonglog.domain.walk.entity.WalkPosition;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface WalkPositionRepository extends JpaRepository<WalkPosition, Long> {
//    @Query("SELECT wp FROM WalkPosition wp WHERE wp.track.id = :trackId ORDER BY wp.createdAt DESC LIMIT 1")
//    Optional<WalkPosition> findLastPositionByTrack(@Param("trackId") Long trackId);

    Optional<WalkPosition> findTopByTrackOrderByCreatedAtDesc(Track track);
}
