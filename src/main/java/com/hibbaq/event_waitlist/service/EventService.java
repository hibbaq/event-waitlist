package com.hibbaq.event_waitlist.service;

import com.hibbaq.event_waitlist.model.Event;
import com.hibbaq.event_waitlist.model.User;
import com.hibbaq.event_waitlist.repository.EventRepository;
import com.hibbaq.event_waitlist.repository.UserRepository;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class EventService {

    private final EventRepository eventRepository;
    private final UserRepository userRepository;

    public EventService(EventRepository eventRepository, UserRepository userRepository) {
        this.eventRepository = eventRepository;
        this.userRepository = userRepository;
    }

    public Event createEvent(String title, String description, String location, 
                             LocalDateTime eventDateTime, Integer maxCapacity, Long organizerId) {
        User organizer = userRepository.findById(organizerId)
            .orElseThrow(() -> new RuntimeException("Organizer not found"));
        
        Event event = new Event(title, description, location, eventDateTime, maxCapacity, organizer);
        return eventRepository.save(event);
    }

    public List<Event> getAllEvents() {
        return eventRepository.findAll();
    }

    public Event getEventById(Long id) {
        return eventRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Event not found"));
    }

    public List<Event> getUpcomingEvents() {
        return eventRepository.findByEventDateTimeAfter(LocalDateTime.now());
    }

    public void deleteEvent(Long id) {
        eventRepository.deleteById(id);
    }
}
