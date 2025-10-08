package com.example.studio.model;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class UserProfile {
    private Long id;
    private Long userId;
    private String mainInstrument;
    private String subInstruments;
    private String favoriteBands;
    private String gender;
    private Integer age;
    private String comment;
    private LocalDateTime updatedAt;

    public List<String> getSubInstrumentsList() {
        if (subInstruments == null || subInstruments.isEmpty()) {
            return List.of();
        }
        return Arrays.stream(subInstruments.split(","))
                .map(String::trim)
                .filter(s -> !s.isEmpty())
                .collect(Collectors.toList());
    }

    public List<String> getFavoriteBandsList() {
        if (favoriteBands == null || favoriteBands.isEmpty()) {
            return List.of();
        }
        return Arrays.stream(favoriteBands.split(","))
                .map(String::trim)
                .filter(s -> !s.isEmpty())
                .collect(Collectors.toList());
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }

    public String getMainInstrument() { return mainInstrument; }
    public void setMainInstrument(String mainInstrument) { this.mainInstrument = mainInstrument; }

    public String getSubInstruments() { return subInstruments; }
    public void setSubInstruments(String subInstruments) { this.subInstruments = subInstruments; }

    public String getFavoriteBands() { return favoriteBands; }
    public void setFavoriteBands(String favoriteBands) { this.favoriteBands = favoriteBands; }

    public String getGender() { return gender; }
    public void setGender(String gender) { this.gender = gender; }

    public Integer getAge() { return age; }
    public void setAge(Integer age) { this.age = age; }

    public String getComment() { return comment; }
    public void setComment(String comment) { this.comment = comment; }

    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
}