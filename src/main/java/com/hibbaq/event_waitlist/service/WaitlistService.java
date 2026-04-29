package com.hibbaq.event_waitlist.service;

import com.hibbaq.event_waitlist.model.*;
import com.hibbaq.event_waitlist.repository.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
public class WaitlistService {

    private final EventRepository eventRepository;
    private final WaitlistEntryRepository waitlistEntryRepository;
    private final UserRepository userRepository;

    public WaitlistService(EventRepository eventRepository, 
                           WaitlistEntryRepository waitlistEntryRepository,
                           UserRepository userRepository) {
        this.eventRepository = eventRepository;
        this.waitlistEntryRepository = waitlistEntryRepository;
        this.userRepository = userRepository;
    }

    @Transactional
    public String joinWaitlist(Long eventId, Long userId) {
        Event event = eventRepository.findById(eventId)
            .orElseThrow(() -> new RuntimeException("Event not found"));
        
        User user = userRepository.findById(userId)
            .orElseThrow(() -> new RuntimeException("User not found"));
        
        // Check if already on waitlist or confirmed
        boolean alreadyWaiting = waitlistEntryRepository
            .existsByEventAndUserIdAndStatus(event, userId, "WAITING");
        boolean alreadyConfirmed = waitlistEntryRepository
            .existsByEventAndUserIdAndStatus(event, userId, "CONFIRMED");
        
        if (alreadyWaiting) return "Already on waitlist";
        if (alreadyConfirmed) return "Already confirmed for this event";
        
        // Count current confirmed attendees
        long confirmedCount = waitlistEntryRepository
            .countByEventAndStatus(event, "CONFIRMED");
        
        // If spots available, confirm immediately
        if (confirmedCount < event.getMaxCapacity()) {
            WaitlistEntry entry = new WaitlistEntry(user, event, null, "CONFIRMED");
            waitlistEntryRepository.save(entry);
            return "✅ Spot confirmed! You're in for " + event.getTitle();
        }
        
        // Otherwise, add to waitlist with position
        long waitlistCount = waitlistEntryRepository
            .countByEventAndStatus(event, "WAITING");
        int newPosition = (int) waitlistCount + 1;
        
        WaitlistEntry entry = new WaitlistEntry(user, event, newPosition, "WAITING");
        waitlistEntryRepository.save(entry);
        
        return "⏳ Added to waitlist. Position: " + newPosition + " for " + event.getTitle();
    }

    @Transactional
    public void autoPromoteNext(Long eventId) {
        Event event = eventRepository.findById(eventId)
            .orElseThrow(() -> new RuntimeException("Event not found"));
        
        long confirmedCount = waitlistEntryRepository
            .countByEventAndStatus(event, "CONFIRMED");
        
        if (confirmedCount < event.getMaxCapacity()) {
            List<WaitlistEntry> waiting = waitlistEntryRepository
                .findByEventOrderByPositionAsc(event)
                .stream()
                .filter(e -> "WAITING".equals(e.getStatus()))
                .toList();
            
            if (!waiting.isEmpty()) {
                WaitlistEntry next = waiting.get(0);
                next.setStatus("CONFIRMED");
                next.setPosition(null);
                waitlistEntryRepository.save(next);
                
                // Reorder remaining waitlist
                List<WaitlistEntry> remaining = waitlistEntryRepository
                    .findByEventOrderByPositionAsc(event)
                    .stream()
                    .filter(e -> "WAITING".equals(e.getStatus()))
                    .toList();
                
                int newPos = 1;
                for (WaitlistEntry entry : remaining) {
                    entry.setPosition(newPos++);
                    waitlistEntryRepository.save(entry);
                }
                
                System.out.println("🎉 AUTO-PROMOTED: " + next.getUser().getName() + " to " + event.getTitle());
            }
        }
    }

    @Transactional
    public String cancelSpot(Long eventId, Long userId) {
        List<WaitlistEntry> entries = waitlistEntryRepository.findByEventOrderByPositionAsc(
            eventRepository.findById(eventId).orElseThrow()
        );
        
        WaitlistEntry userEntry = entries.stream()
            .filter(e -> e.getUser().getId().equals(userId))
            .findFirst()
            .orElseThrow(() -> new RuntimeException("No entry found"));
        
        String previousStatus = userEntry.getStatus();
        userEntry.setStatus("CANCELLED");
        waitlistEntryRepository.save(userEntry);
        
        // If they were confirmed, auto-promote the next person
        if ("CONFIRMED".equals(previousStatus)) {
            autoPromoteNext(eventId);
        }
        
        return "❌ Cancelled. " + (previousStatus.equals("CONFIRMED") ? "Next person promoted!" : "Removed from waitlist.");
    }

    public int getQueuePosition(Long eventId, Long userId) {
        Event event = eventRepository.findById(eventId).orElseThrow();
        WaitlistEntry entry = waitlistEntryRepository
            .findByEventAndUserIdAndStatus(event, userId, "WAITING");
        
        if (entry == null) return -1;
        return entry.getPosition();
    }
    
    public List<WaitlistEntry> getWaitlistForEvent(Long eventId) {
        Event event = eventRepository.findById(eventId).orElseThrow();
        return waitlistEntryRepository.findByEventOrderByPositionAsc(event);
    }
}
