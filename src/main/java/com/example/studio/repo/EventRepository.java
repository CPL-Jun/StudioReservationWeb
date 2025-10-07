package com.example.studio.repo;

import com.example.studio.model.Event;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Repository
public class EventRepository {

    private final JdbcTemplate jdbc;
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public EventRepository(JdbcTemplate jdbc) {
        this.jdbc = jdbc;
    }

    private static final RowMapper<Event> ROW_MAPPER = new RowMapper<Event>() {
        @Override
        public Event mapRow(ResultSet rs, int rowNum) throws SQLException {
            Event event = new Event();
            event.setId(rs.getLong("id"));
            event.setTitle(rs.getString("title"));
            event.setDescription(rs.getString("description"));
            
            // StringをLocalDateTimeに変換
            String eventDateStr = rs.getString("event_date");
            if (eventDateStr != null) {
                try {
                    event.setEventDate(LocalDateTime.parse(eventDateStr.replace("T", " ").substring(0, 19), FORMATTER));
                } catch (Exception e) {
                    event.setEventDate(LocalDateTime.now());
                }
            }
            
            event.setVenue(rs.getString("venue"));
            event.setImageUrl(rs.getString("image_url"));
            event.setCapacity(rs.getInt("capacity"));
            event.setCurrentCount(rs.getInt("current_count"));
            event.setCreatedBy(rs.getString("created_by"));
            
            // created_atとupdated_atの変換
            String createdAtStr = rs.getString("created_at");
            if (createdAtStr != null) {
                try {
                    event.setCreatedAt(LocalDateTime.parse(createdAtStr.replace("T", " ").substring(0, 19), FORMATTER));
                } catch (Exception e) {
                    event.setCreatedAt(LocalDateTime.now());
                }
            }
            
            String updatedAtStr = rs.getString("updated_at");
            if (updatedAtStr != null) {
                try {
                    event.setUpdatedAt(LocalDateTime.parse(updatedAtStr.replace("T", " ").substring(0, 19), FORMATTER));
                } catch (Exception e) {
                    event.setUpdatedAt(LocalDateTime.now());
                }
            }
            
            return event;
        }
    };

    public List<Event> findAll() {
        return jdbc.query("SELECT * FROM events ORDER BY event_date DESC", ROW_MAPPER);
    }

    public Event findById(Long id) {
        List<Event> results = jdbc.query(
            "SELECT * FROM events WHERE id = ?", ROW_MAPPER, id);
        return results.isEmpty() ? null : results.get(0);
    }

    public void insert(Event event) {
        jdbc.update(
            "INSERT INTO events (title, description, event_date, venue, image_url, capacity, current_count, created_by, created_at, updated_at) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)",
            event.getTitle(),
            event.getDescription(),
            event.getEventDate() != null ? event.getEventDate().toString() : null,
            event.getVenue(),
            event.getImageUrl(),
            event.getCapacity(),
            event.getCurrentCount() != null ? event.getCurrentCount() : 0,
            event.getCreatedBy(),
            LocalDateTime.now().toString(),
            LocalDateTime.now().toString()
        );
    }

    public void update(Event event) {
        jdbc.update(
            "UPDATE events SET title = ?, description = ?, event_date = ?, venue = ?, image_url = ?, capacity = ?, current_count = ?, updated_at = ? WHERE id = ?",
            event.getTitle(),
            event.getDescription(),
            event.getEventDate() != null ? event.getEventDate().toString() : null,
            event.getVenue(),
            event.getImageUrl(),
            event.getCapacity(),
            event.getCurrentCount(),
            LocalDateTime.now().toString(),
            event.getId()
        );
    }

    public void deleteById(Long id) {
        jdbc.update("DELETE FROM events WHERE id = ?", id);
    }
}