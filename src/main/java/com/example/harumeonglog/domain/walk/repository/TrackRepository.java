package com.example.harumeonglog.domain.walk.repository;

import com.example.harumeonglog.domain.walk.entity.Track;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface TrackRepository extends JpaRepository<Track, Long> {
    @Query("SELECT t FROM Track t join fetch t.walk w WHERE t.id = :trackId")
    Optional<Track> findWithWalk(@Param("trackId") Long trackId);
}
