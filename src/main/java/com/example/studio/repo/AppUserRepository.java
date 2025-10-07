package com.example.studio.repo;

import com.example.studio.model.AppUser;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Repository
public class AppUserRepository {

    private final JdbcTemplate jdbc;
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public AppUserRepository(JdbcTemplate jdbc) {
        this.jdbc = jdbc;
    }

    private static final RowMapper<AppUser> ROW_MAPPER = new RowMapper<AppUser>() {
        @Override
        public AppUser mapRow(ResultSet rs, int rowNum) throws SQLException {
            AppUser user = new AppUser();
            user.setId(rs.getLong("id"));
            user.setEmail(rs.getString("email"));
            user.setPasswordHash(rs.getString("password_hash"));
            user.setDisplayName(rs.getString("display_name"));
            user.setRole(rs.getString("role"));
            
            // 文字列をLocalDateTimeに変換
            String createdAtStr = rs.getString("created_at");
            if (createdAtStr != null) {
                try {
                    user.setCreatedAt(LocalDateTime.parse(createdAtStr.replace("T", " ").substring(0, 19), FORMATTER));
                } catch (Exception e) {
                    // パースエラーの場合は現在時刻を設定
                    user.setCreatedAt(LocalDateTime.now());
                }
            }
            return user;
        }
    };

    public List<AppUser> findAll() {
        return jdbc.query("SELECT * FROM app_users", ROW_MAPPER);
    }

    public AppUser findById(Long id) {
        List<AppUser> results = jdbc.query(
            "SELECT * FROM app_users WHERE id = ?", ROW_MAPPER, id);
        return results.isEmpty() ? null : results.get(0);
    }

    public AppUser findByEmail(String email) {
        List<AppUser> results = jdbc.query(
            "SELECT * FROM app_users WHERE email = ?", ROW_MAPPER, email);
        return results.isEmpty() ? null : results.get(0);
    }

    public void insert(String email, String passwordHash, String displayName, String role) {
        jdbc.update(
            "INSERT INTO app_users (email, password_hash, display_name, role) VALUES (?, ?, ?, ?)",
            email, passwordHash, displayName, role
        );
    }

    public void save(AppUser user) {
        if (user.getId() == null) {
            // 新規作成
            insert(user.getEmail(), user.getPasswordHash(), user.getDisplayName(), user.getRole());
        } else {
            // 更新
            update(user);
        }
    }

    public void update(AppUser user) {
        jdbc.update(
            "UPDATE app_users SET email = ?, password_hash = ?, display_name = ?, role = ? WHERE id = ?",
            user.getEmail(), user.getPasswordHash(), user.getDisplayName(), user.getRole(), user.getId()
        );
    }

    public void deleteById(Long id) {
        jdbc.update("DELETE FROM app_users WHERE id = ?", id);
    }
}