package com.example.studio.repo;

import com.example.studio.model.BbsPost;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Repository
public class BbsRepository {

    private final JdbcTemplate jdbc;
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public BbsRepository(JdbcTemplate jdbc) {
        this.jdbc = jdbc;
    }

    private static final RowMapper<BbsPost> ROW_MAPPER = new RowMapper<BbsPost>() {
        @Override
        public BbsPost mapRow(ResultSet rs, int rowNum) throws SQLException {
            BbsPost post = new BbsPost();
            post.setId(rs.getLong("id"));
            post.setName(rs.getString("name"));
            post.setSong(rs.getString("song"));
            post.setBand(rs.getString("band"));
            post.setComment(rs.getString("comment"));
            
            // StringをLocalDateTimeに変換
            String createdAtStr = rs.getString("created_at");
            if (createdAtStr != null) {
                try {
                    post.setCreatedAt(LocalDateTime.parse(createdAtStr.replace("T", " ").substring(0, 19), FORMATTER));
                } catch (Exception e) {
                    post.setCreatedAt(LocalDateTime.now());
                }
            }
            return post;
        }
    };

    public List<BbsPost> findAll() {
        return jdbc.query("SELECT * FROM bbs_posts ORDER BY created_at DESC", ROW_MAPPER);
    }

    public BbsPost findById(Long id) {
        List<BbsPost> results = jdbc.query(
            "SELECT * FROM bbs_posts WHERE id = ?", ROW_MAPPER, id);
        return results.isEmpty() ? null : results.get(0);
    }

    public void insert(BbsPost post) {
        jdbc.update(
            "INSERT INTO bbs_posts (name, song, band, comment, created_at) VALUES (?, ?, ?, ?, ?)",
            post.getName(),
            post.getSong(),
            post.getBand(),
            post.getComment(),
            LocalDateTime.now().toString()
        );
    }

    public void update(BbsPost post) {
        jdbc.update(
            "UPDATE bbs_posts SET name = ?, song = ?, band = ?, comment = ? WHERE id = ?",
            post.getName(),
            post.getSong(),
            post.getBand(),
            post.getComment(),
            post.getId()
        );
    }

    public void deleteById(Long id) {
        jdbc.update("DELETE FROM bbs_posts WHERE id = ?", id);
    }
}