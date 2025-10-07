package com.example.studio.repo;

import com.example.studio.model.Profile;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Repository
public class ProfileRepository {

    private final JdbcTemplate jdbc;

    public ProfileRepository(JdbcTemplate jdbc) {
        this.jdbc = jdbc;
    }

    private static final RowMapper<Profile> ROW_MAPPER = new RowMapper<Profile>() {
        @Override
        public Profile mapRow(ResultSet rs, int rowNum) throws SQLException {
            Profile p = new Profile();
            p.setId(rs.getLong("id"));
            p.setName(rs.getString("name"));
            p.setInstrument(rs.getString("instrument"));
            p.setGenre(rs.getString("genre"));
            p.setBio(rs.getString("bio"));
            return p;
        }
    };

    public List<Profile> findAll() {
        return jdbc.query("SELECT * FROM profiles", ROW_MAPPER);
    }

    public Profile findById(Long id) {
        List<Profile> results = jdbc.query(
            "SELECT * FROM profiles WHERE id = ?", ROW_MAPPER, id);
        return results.isEmpty() ? null : results.get(0);
    }

    public void insert(Profile profile) {
        jdbc.update(
            "INSERT INTO profiles (name, instrument, genre, bio) VALUES (?, ?, ?, ?)",
            profile.getName(), profile.getInstrument(), profile.getGenre(), profile.getBio()
        );
    }

    public void update(Profile profile) {
        jdbc.update(
            "UPDATE profiles SET name = ?, instrument = ?, genre = ?, bio = ? WHERE id = ?",
            profile.getName(), profile.getInstrument(), profile.getGenre(), profile.getBio(), profile.getId()
        );
    }

    public void deleteById(Long id) {
        jdbc.update("DELETE FROM profiles WHERE id = ?", id);
    }
}