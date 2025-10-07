package com.example.studio.model;

import java.time.LocalDateTime;

public class Reservation {
    private Long id;
    private String venue;
    private String room;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private String name;

    public Reservation() {}

    public Reservation(String venue, String room, LocalDateTime startTime, 
                      LocalDateTime endTime, String name) {
        this.venue = venue;
        this.room = room;
        this.startTime = startTime;
        this.endTime = endTime;
        this.name = name;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getVenue() { return venue; }
    public void setVenue(String venue) { this.venue = venue; }

    public String getRoom() { return room; }
    public void setRoom(String room) { this.room = room; }

    public LocalDateTime getStartTime() { return startTime; }
    public void setStartTime(LocalDateTime startTime) { this.startTime = startTime; }

    public LocalDateTime getEndTime() { return endTime; }
    public void setEndTime(LocalDateTime endTime) { this.endTime = endTime; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
}
