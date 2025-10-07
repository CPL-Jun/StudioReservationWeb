package com.example.studio.repo;

import com.example.studio.model.UserProfile;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Repository
public class UserProfileRepository {

    private final JdbcTemplate jdbc;
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public UserProfileRepository(JdbcTemplate jdbc) {
        this.jdbc = jdbc;
    }

    private static final RowMapper<UserProfile> ROW_MAPPER = new RowMapper<UserProfile>() {
        @Override
        public UserProfile mapRow(ResultSet rs, int rowNum) throws SQLException {
            UserProfile profile = new UserProfile();
            profile.setId(rs.getLong("id"));
            profile.setUserId(rs.getLong("user_id"));
            profile.setMainInstrument(rs.getString("main_instrument"));
            profile.setSubInstruments(rs.getString("sub_instruments"));
            profile.setFavoriteBands(rs.getString("favorite_bands"));
            profile.setGender(rs.getString("gender"));
            
            int age = rs.getInt("age");
            if (!rs.wasNull()) {
                profile.setAge(age);
            }
            
            // StringをLocalDateTimeに変換
            String updatedAtStr = rs.getString("updated_at");
            if (updatedAtStr != null) {
                try {
                    profile.setUpdatedAt(LocalDateTime.parse(updatedAtStr.replace("T", " ").substring(0, 19), FORMATTER));
                } catch (Exception e) {
                    profile.setUpdatedAt(LocalDateTime.now());
                }
            }
            
            return profile;
        }
    };

    public List<UserProfile> findAll() {
        return jdbc.query("SELECT * FROM user_profiles", ROW_MAPPER);
    }

    public UserProfile findById(Long id) {
        List<UserProfile> results = jdbc.query(
            "SELECT * FROM user_profiles WHERE id = ?", ROW_MAPPER, id);
        return results.isEmpty() ? null : results.get(0);
    }

    public UserProfile findByUserId(Long userId) {
        List<UserProfile> results = jdbc.query(
            "SELECT * FROM user_profiles WHERE user_id = ?", ROW_MAPPER, userId);
        return results.isEmpty() ? null : results.get(0);
    }

    public void insert(UserProfile profile) {
        jdbc.update(
            "INSERT INTO user_profiles (user_id, main_instrument, sub_instruments, favorite_bands, gender, age, updated_at) VALUES (?, ?, ?, ?, ?, ?, ?)",
            profile.getUserId(),
            profile.getMainInstrument(),
            profile.getSubInstruments(),
            profile.getFavoriteBands(),
            profile.getGender(),
            profile.getAge(),
            LocalDateTime.now().toString()
        );
    }

    public void update(UserProfile profile) {
        jdbc.update(
            "UPDATE user_profiles SET main_instrument = ?, sub_instruments = ?, favorite_bands = ?, gender = ?, age = ?, updated_at = ? WHERE id = ?",
            profile.getMainInstrument(),
            profile.getSubInstruments(),
            profile.getFavoriteBands(),
            profile.getGender(),
            profile.getAge(),
            LocalDateTime.now().toString(),
            profile.getId()
        );
    }

    public void deleteById(Long id) {
        jdbc.update("DELETE FROM user_profiles WHERE id = ?", id);
    }
}