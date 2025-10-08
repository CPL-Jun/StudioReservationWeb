package com.example.studio.model;

import java.time.LocalDateTime;

public class Reservation {
    private Long id;
    private String venue;
    private String room;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private String name;
    private String representative;  // 代表者名（Roxette用）
    private String bandName;        // バンド名（Roxette用）
    private Boolean isSlot;         // 枠かどうか
    private Integer performanceTime; // 演奏時間（分）
    private Integer changeoverTime;  // 転換時間（分）
    private Long slotId;            // どの枠に属するか

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

    public String getRepresentative() { return representative; }
    public void setRepresentative(String representative) { this.representative = representative; }

    public String getBandName() { return bandName; }
    public void setBandName(String bandName) { this.bandName = bandName; }

    public Boolean getIsSlot() { return isSlot; }
    public void setIsSlot(Boolean isSlot) { this.isSlot = isSlot; }

    public Integer getPerformanceTime() { return performanceTime; }
    public void setPerformanceTime(Integer performanceTime) { this.performanceTime = performanceTime; }

    public Integer getChangeoverTime() { return changeoverTime; }
    public void setChangeoverTime(Integer changeoverTime) { this.changeoverTime = changeoverTime; }

    public Long getSlotId() { return slotId; }
    public void setSlotId(Long slotId) { this.slotId = slotId; }
}