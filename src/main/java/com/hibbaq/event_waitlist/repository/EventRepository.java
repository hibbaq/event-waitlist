package com.hibbaq.event_waitlist.repository;

import com.hibbaq.event_waitlist.model.Event;
import org.springframework.data.jpa.repository.JpaRepository;
import java.time.LocalDateTime;
import java.util.List;

public interface EventRepository extends JpaRepository<Event, Long> {
    List<Event> findByEventDateTimeAfter(LocalDateTime date);
    List<Event> findByOrganizerId(Long organizerId);
}
