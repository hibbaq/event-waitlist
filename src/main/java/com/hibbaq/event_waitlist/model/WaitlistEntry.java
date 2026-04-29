package com.hibbaq.event_waitlist.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "waitlist_entries")
public class WaitlistEntry {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne
    private User user;
    
    @ManyToOne
    private Event event;
    
    private Integer position;
    private LocalDateTime joinedAt;
    private String status;
    
    public WaitlistEntry() {}
    
    public WaitlistEntry(User user, Event event, Integer position, String status) {
        this.user = user;
        this.event = event;
        this.position = position;
        this.status = status;
        this.joinedAt = LocalDateTime.now();
    }
    
    // Getters
    public Long getId() { return id; }
    public User getUser() { return user; }
    public Event getEvent() { return event; }
    public Integer getPosition() { return position; }
    public LocalDateTime getJoinedAt() { return joinedAt; }
    public String getStatus() { return status; }
    
    // Setters
    public void setId(Long id) { this.id = id; }
    public void setUser(User user) { this.user = user; }
    public void setEvent(Event event) { this.event = event; }
    public void setPosition(Integer position) { this.position = position; }
    public void setJoinedAt(LocalDateTime joinedAt) { this.joinedAt = joinedAt; }
    public void setStatus(String status) { this.status = status; }
}