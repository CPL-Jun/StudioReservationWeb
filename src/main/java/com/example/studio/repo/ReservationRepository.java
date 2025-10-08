package com.example.studio.repo;

import com.example.studio.model.Reservation;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public class ReservationRepository {

    private final JdbcTemplate jdbc;

    public ReservationRepository(JdbcTemplate jdbc) {
        this.jdbc = jdbc;
    }

    private static final RowMapper<Reservation> ROW_MAPPER = new RowMapper<Reservation>() {
        @Override
        public Reservation mapRow(ResultSet rs, int rowNum) throws SQLException {
            Reservation r = new Reservation();
            r.setId(rs.getLong("id"));
            r.setVenue(rs.getString("venue"));
            r.setRoom(rs.getString("room"));
            r.setStartTime(LocalDateTime.parse(rs.getString("start_time")));
            r.setEndTime(LocalDateTime.parse(rs.getString("end_time")));
            r.setName(rs.getString("name"));
            r.setRepresentative(rs.getString("representative"));
            r.setBandName(rs.getString("band_name"));
            
            int isSlot = rs.getInt("is_slot");
            r.setIsSlot(isSlot == 1);
            
            int performanceTime = rs.getInt("performance_time");
            if (!rs.wasNull()) {
                r.setPerformanceTime(performanceTime);
            }
            
            int changeoverTime = rs.getInt("changeover_time");
            if (!rs.wasNull()) {
                r.setChangeoverTime(changeoverTime);
            }
            
            long slotId = rs.getLong("slot_id");
            if (!rs.wasNull()) {
                r.setSlotId(slotId);
            }
            
            return r;
        }
    };

    public List<Reservation> findAll() {
        return jdbc.query("SELECT * FROM reservations ORDER BY start_time", ROW_MAPPER);
    }

    public Reservation findById(Long id) {
        List<Reservation> results = jdbc.query(
            "SELECT * FROM reservations WHERE id = ?", ROW_MAPPER, id);
        return results.isEmpty() ? null : results.get(0);
    }

    public List<Reservation> findByRoom(String room) {
        return jdbc.query(
            "SELECT * FROM reservations WHERE room = ? ORDER BY start_time", 
            ROW_MAPPER, room);
    }

    public List<Reservation> findInRange(String room, String startDate, String endDate) {
        return jdbc.query(
            "SELECT * FROM reservations WHERE room = ? AND start_time >= ? AND end_time <= ? ORDER BY start_time",
            ROW_MAPPER, room, startDate, endDate);
    }

    public void insert(Reservation reservation) {
        jdbc.update(
            "INSERT INTO reservations (venue, room, start_time, end_time, name, representative, band_name, is_slot, performance_time, changeover_time, slot_id) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)",
            reservation.getVenue(),
            reservation.getRoom(),
            reservation.getStartTime().toString(),
            reservation.getEndTime().toString(),
            reservation.getName(),
            reservation.getRepresentative(),
            reservation.getBandName(),
            reservation.getIsSlot() != null && reservation.getIsSlot() ? 1 : 0,
            reservation.getPerformanceTime(),
            reservation.getChangeoverTime(),
            reservation.getSlotId()
        );
    }

    public void update(Reservation reservation) {
        jdbc.update(
            "UPDATE reservations SET venue = ?, room = ?, start_time = ?, end_time = ?, name = ?, representative = ?, band_name = ?, is_slot = ?, performance_time = ?, changeover_time = ?, slot_id = ? WHERE id = ?",
            reservation.getVenue(),
            reservation.getRoom(),
            reservation.getStartTime().toString(),
            reservation.getEndTime().toString(),
            reservation.getName(),
            reservation.getRepresentative(),
            reservation.getBandName(),
            reservation.getIsSlot() != null && reservation.getIsSlot() ? 1 : 0,
            reservation.getPerformanceTime(),
            reservation.getChangeoverTime(),
            reservation.getSlotId(),
            reservation.getId()
        );
    }

    public void deleteById(Long id) {
        jdbc.update("DELETE FROM reservations WHERE id = ?", id);
    }
}