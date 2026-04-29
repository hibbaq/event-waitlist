package com.hibbaq.event_waitlist.config;

import com.hibbaq.event_waitlist.model.*;
import com.hibbaq.event_waitlist.repository.*;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import java.time.LocalDateTime;

@Component
public class DataLoader implements CommandLineRunner {

    private final UserRepository userRepository;
    private final EventRepository eventRepository;
    private final WaitlistEntryRepository waitlistEntryRepository;

    public DataLoader(UserRepository userRepository, EventRepository eventRepository, 
                      WaitlistEntryRepository waitlistEntryRepository) {
        this.userRepository = userRepository;
        this.eventRepository = eventRepository;
        this.waitlistEntryRepository = waitlistEntryRepository;
    }

    @Override
    public void run(String... args) {
        // Create users
        User organizer1 = new User("organizer@events.com", "Sarah Chen", "pass123", User.Role.ORGANIZER);
        User organizer2 = new User("campus@events.com", "Mike Ross", "pass123", User.Role.ORGANIZER);
        User user1 = new User("alice@example.com", "Alice Kim", "pass123", User.Role.USER);
        User user2 = new User("bob@example.com", "Bob Lee", "pass123", User.Role.USER);
        User user3 = new User("carol@example.com", "Carol David", "pass123", User.Role.USER);
        
        userRepository.save(organizer1);
        userRepository.save(organizer2);
        userRepository.save(user1);
        userRepository.save(user2);
        userRepository.save(user3);
        
        // Create events
        Event event1 = new Event(
            "Career Fair 2026", 
            "Meet top employers hiring for tech roles", 
            "Student Center, Room 200", 
            LocalDateTime.of(2026, 5, 15, 10, 0), 
            2,  // Small capacity to test waitlist
            organizer1
        );
        
        Event event2 = new Event(
            "Hackathon Weekend", 
            "Build something amazing in 48 hours", 
            "Engineering Building, Floor 3", 
            LocalDateTime.of(2026, 6, 1, 9, 0), 
            100, 
            organizer1
        );
        
        Event event3 = new Event(
            "Yoga Workshop", 
            "Free stress relief session", 
            "Gym Studio A", 
            LocalDateTime.of(2026, 4, 25, 14, 0), 
            20, 
            organizer2
        );
        
        eventRepository.save(event1);
        eventRepository.save(event2);
        eventRepository.save(event3);
        
        // Create waitlist entries (event1 capacity is 2, so 3rd person goes to waitlist)
        WaitlistEntry entry1 = new WaitlistEntry(user1, event1, null, "CONFIRMED");
        WaitlistEntry entry2 = new WaitlistEntry(user2, event1, null, "CONFIRMED");
        WaitlistEntry entry3 = new WaitlistEntry(user3, event1, 1, "WAITING");
        
        waitlistEntryRepository.save(entry1);
        waitlistEntryRepository.save(entry2);
        waitlistEntryRepository.save(entry3);
        
        System.out.println("✅ Data loaded!");
        System.out.println("   Users: " + userRepository.count());
        System.out.println("   Events: " + eventRepository.count());
        System.out.println("   Waitlist entries: " + waitlistEntryRepository.count());
    }
}
