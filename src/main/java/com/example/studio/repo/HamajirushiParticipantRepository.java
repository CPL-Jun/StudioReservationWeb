package com.example.studio.repo;

import com.example.studio.model.HamajirushiParticipant;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Repository
public class HamajirushiParticipantRepository {

    private final JdbcTemplate jdbc;
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private static final DateTimeFormatter DATETIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public HamajirushiParticipantRepository(JdbcTemplate jdbc) {
        this.jdbc = jdbc;
    }

    private static final RowMapper<HamajirushiParticipant> ROW_MAPPER = new RowMapper<HamajirushiParticipant>() {
        @Override
        public HamajirushiParticipant mapRow(ResultSet rs, int rowNum) throws SQLException {
            HamajirushiParticipant participant = new HamajirushiParticipant();
            participant.setId(rs.getLong("id"));
            
            String dateStr = rs.getString("date");
            if (dateStr != null) {
                participant.setDate(LocalDate.parse(dateStr, DATE_FORMATTER));
            }
            
            participant.setUserId(rs.getLong("user_id"));
            
            String createdAtStr = rs.getString("created_at");
            if (createdAtStr != null) {
                try {
                    participant.setCreatedAt(LocalDateTime.parse(createdAtStr.replace("T", " ").substring(0, 19), DATETIME_FORMATTER));
                } catch (Exception e) {
                    participant.setCreatedAt(LocalDateTime.now());
                }
            }
            
            return participant;
        }
    };

    private static final RowMapper<HamajirushiParticipant> ROW_MAPPER_WITH_USER = new RowMapper<HamajirushiParticipant>() {
        @Override
        public HamajirushiParticipant mapRow(ResultSet rs, int rowNum) throws SQLException {
            HamajirushiParticipant participant = ROW_MAPPER.mapRow(rs, rowNum);
            participant.setUserName(rs.getString("user_name"));
            participant.setUserEmail(rs.getString("user_email"));
            return participant;
        }
    };

    public List<HamajirushiParticipant> findByDate(LocalDate date) {
        String sql = "SELECT hp.*, au.display_name as user_name, au.email as user_email " +
                     "FROM hamajirushi_participants hp " +
                     "JOIN app_users au ON hp.user_id = au.id " +
                     "WHERE hp.date = ? " +
                     "ORDER BY hp.created_at";
        return jdbc.query(sql, ROW_MAPPER_WITH_USER, date.format(DATE_FORMATTER));
    }

    public List<HamajirushiParticipant> findByMonth(int year, int month) {
        String startDate = String.format("%04d-%02d-01", year, month);
        String endDate = String.format("%04d-%02d-31", year, month);
        
        String sql = "SELECT * FROM hamajirushi_participants " +
                     "WHERE date >= ? AND date <= ? " +
                     "ORDER BY date, created_at";
        return jdbc.query(sql, ROW_MAPPER, startDate, endDate);
    }

    public boolean existsByDateAndUserId(LocalDate date, Long userId) {
        String sql = "SELECT COUNT(*) FROM hamajirushi_participants WHERE date = ? AND user_id = ?";
        Integer count = jdbc.queryForObject(sql, Integer.class, date.format(DATE_FORMATTER), userId);
        return count != null && count > 0;
    }

    public void insert(LocalDate date, Long userId) {
        String sql = "INSERT INTO hamajirushi_participants (date, user_id, created_at) VALUES (?, ?, ?)";
        jdbc.update(sql, date.format(DATE_FORMATTER), userId, LocalDateTime.now().toString());
    }

    public void deleteByDateAndUserId(LocalDate date, Long userId) {
        String sql = "DELETE FROM hamajirushi_participants WHERE date = ? AND user_id = ?";
        jdbc.update(sql, date.format(DATE_FORMATTER), userId);
    }

    public int countByDate(LocalDate date) {
        String sql = "SELECT COUNT(*) FROM hamajirushi_participants WHERE date = ?";
        Integer count = jdbc.queryForObject(sql, Integer.class, date.format(DATE_FORMATTER));
        return count != null ? count : 0;
    }
}