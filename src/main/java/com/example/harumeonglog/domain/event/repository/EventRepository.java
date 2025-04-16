package com.example.harumeonglog.domain.event.repository;

import com.example.harumeonglog.domain.event.entity.Event;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EventRepository extends JpaRepository<Event, Long> {
}
