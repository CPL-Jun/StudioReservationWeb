package com.example.studio.model;

import java.time.LocalDateTime;

public class BbsPost {
    private Long id;
    private String name;
    private String song;
    private String band;
    private String comment;
    private LocalDateTime createdAt;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getSong() { return song; }
    public void setSong(String song) { this.song = song; }

    public String getBand() { return band; }
    public void setBand(String band) { this.band = band; }

    public String getComment() { return comment; }
    public void setComment(String comment) { this.comment = comment; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}
