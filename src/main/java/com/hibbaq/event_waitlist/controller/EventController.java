package com.hibbaq.event_waitlist.controller;

import com.hibbaq.event_waitlist.dto.CreateEventRequest;
import com.hibbaq.event_waitlist.model.Event;
import com.hibbaq.event_waitlist.service.EventService;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/events")
public class EventController {

    private final EventService eventService;

    public EventController(EventService eventService) {
        this.eventService = eventService;
    }

    @GetMapping
    public List<Event> getAllEvents() {
        return eventService.getAllEvents();
    }

    @GetMapping("/upcoming")
    public List<Event> getUpcomingEvents() {
        return eventService.getUpcomingEvents();
    }

    @GetMapping("/{id}")
    public Event getEventById(@PathVariable Long id) {
        return eventService.getEventById(id);
    }

    @PostMapping
    public Event createEvent(@RequestBody CreateEventRequest request) {
        return eventService.createEvent(
            request.getTitle(),
            request.getDescription(),
            request.getLocation(),
            request.getEventDateTime(),
            request.getMaxCapacity(),
            request.getOrganizerId()
        );
    }

    @DeleteMapping("/{id}")
    public Map<String, String> deleteEvent(@PathVariable Long id) {
        eventService.deleteEvent(id);
        return Map.of("message", "Event deleted successfully");
    }
}