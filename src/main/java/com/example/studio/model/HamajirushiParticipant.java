package com.example.studio.model;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class HamajirushiParticipant {
    private Long id;
    private LocalDate date;
    private Long userId;
    private LocalDateTime createdAt;

    // ユーザー情報（JOIN用）
    private String userName;
    private String userEmail;

    public HamajirushiParticipant() {}

    public HamajirushiParticipant(LocalDate date, Long userId) {
        this.date = date;
        this.userId = userId;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public LocalDate getDate() { return date; }
    public void setDate(LocalDate date) { this.date = date; }

    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public String getUserName() { return userName; }
    public void setUserName(String userName) { this.userName = userName; }

    public String getUserEmail() { return userEmail; }
    public void setUserEmail(String userEmail) { this.userEmail = userEmail; }
}