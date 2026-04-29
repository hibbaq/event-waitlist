package com.hibbaq.event_waitlist.repository;

import com.hibbaq.event_waitlist.model.WaitlistEntry;
import com.hibbaq.event_waitlist.model.Event;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface WaitlistEntryRepository extends JpaRepository<WaitlistEntry, Long> {
    List<WaitlistEntry> findByEventOrderByPositionAsc(Event event);
    long countByEventAndStatus(Event event, String status);
    boolean existsByEventAndUserIdAndStatus(Event event, Long userId, String status);
    WaitlistEntry findByEventAndUserIdAndStatus(Event event, Long userId, String status);
}
