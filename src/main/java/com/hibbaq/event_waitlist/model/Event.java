package com.hibbaq.event_waitlist.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "events")
public class Event {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private String title;
    private String description;
    private String location;
    private LocalDateTime eventDateTime;
    private Integer maxCapacity;
    
    @ManyToOne
    private User organizer;
    
    @OneToMany(mappedBy = "event", cascade = CascadeType.ALL)
    private List<WaitlistEntry> waitlistEntries = new ArrayList<>();
    
    public Event() {}
    
    public Event(String title, String description, String location, 
                 LocalDateTime eventDateTime, Integer maxCapacity, User organizer) {
        this.title = title;
        this.description = description;
        this.location = location;
        this.eventDateTime = eventDateTime;
        this.maxCapacity = maxCapacity;
        this.organizer = organizer;
    }
    
    // Getters
    public Long getId() { return id; }
    public String getTitle() { return title; }
    public String getDescription() { return description; }
    public String getLocation() { return location; }
    public LocalDateTime getEventDateTime() { return eventDateTime; }
    public Integer getMaxCapacity() { return maxCapacity; }
    public User getOrganizer() { return organizer; }
    public List<WaitlistEntry> getWaitlistEntries() { return waitlistEntries; }
    
    // Setters
    public void setId(Long id) { this.id = id; }
    public void setTitle(String title) { this.title = title; }
    public void setDescription(String description) { this.description = description; }
    public void setLocation(String location) { this.location = location; }
    public void setEventDateTime(LocalDateTime eventDateTime) { this.eventDateTime = eventDateTime; }
    public void setMaxCapacity(Integer maxCapacity) { this.maxCapacity = maxCapacity; }
    public void setOrganizer(User organizer) { this.organizer = organizer; }
    public void setWaitlistEntries(List<WaitlistEntry> waitlistEntries) { this.waitlistEntries = waitlistEntries; }
}
