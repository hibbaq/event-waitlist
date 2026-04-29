package com.hibbaq.event_waitlist.controller;

import com.hibbaq.event_waitlist.dto.JoinWaitlistRequest;
import com.hibbaq.event_waitlist.dto.CancelSpotRequest;
import com.hibbaq.event_waitlist.service.WaitlistService;
import org.springframework.web.bind.annotation.*;
import java.util.Map;

@RestController
@RequestMapping("/api/waitlist")
public class WaitlistController {

    private final WaitlistService waitlistService;

    public WaitlistController(WaitlistService waitlistService) {
        this.waitlistService = waitlistService;
    }

    @PostMapping("/join")
    public Map<String, String> joinWaitlist(@RequestBody JoinWaitlistRequest request) {
        String result = waitlistService.joinWaitlist(request.getEventId(), request.getUserId());
        return Map.of("message", result);
    }

    @PostMapping("/cancel")
    public Map<String, String> cancelSpot(@RequestBody CancelSpotRequest request) {
        String result = waitlistService.cancelSpot(request.getEventId(), request.getUserId());
        return Map.of("message", result);
    }

    @GetMapping("/position")
    public Map<String, Integer> getPosition(@RequestParam Long eventId, @RequestParam Long userId) {
        int position = waitlistService.getQueuePosition(eventId, userId);
        return Map.of("position", position);
    }
    
    @GetMapping("/event/{eventId}")
    public Map<String, Object> getWaitlistForEvent(@PathVariable Long eventId) {
        var entries = waitlistService.getWaitlistForEvent(eventId);
        return Map.of("waitlist", entries, "count", entries.size());
    }
}