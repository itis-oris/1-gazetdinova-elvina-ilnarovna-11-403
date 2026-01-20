package com.oris_sem01.travelplanner.repository.jdbc;

import com.oris_sem01.travelplanner.config.DatabaseConfig;
import com.oris_sem01.travelplanner.model.Schedule;
import com.oris_sem01.travelplanner.repository.ScheduleRepository;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class JdbcScheduleRepository implements ScheduleRepository {

    @Override
    public Schedule save(Schedule schedule) {
        if (schedule.getId() == null) {
            return insert(schedule);
        }
        return schedule;
    }

    private Schedule insert(Schedule schedule) {
        String sql = "INSERT INTO schedules (tour_id, start_date, end_date) VALUES (?, ?, ?) RETURNING id";
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setLong(1, schedule.getTourId());
            ps.setDate(2, Date.valueOf(schedule.getStartDate()));
            ps.setDate(3, Date.valueOf(schedule.getEndDate()));

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    schedule.setId(rs.getLong("id"));
                }
            }
            return schedule;
        } catch (SQLException e) {
            throw new RuntimeException("Ошибка при добавлении расписания", e);
        }
    }

    @Override
    public Optional<Schedule> findById(Long id) {
        String sql = "SELECT id, tour_id, start_date, end_date FROM schedules WHERE id = ?";
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setLong(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapRow(rs));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Ошибка при поиске расписания", e);
        }
        return Optional.empty();
    }

    @Override
    public List<Schedule> findByTourId(Long tourId) {
        String sql = "SELECT id, tour_id, start_date, end_date FROM schedules WHERE tour_id = ? ORDER BY start_date";
        List<Schedule> result = new ArrayList<>();
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setLong(1, tourId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    result.add(mapRow(rs));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Ошибка при получении расписаний по туру", e);
        }
        return result;
    }

    private Schedule mapRow(ResultSet rs) throws SQLException {
        Schedule s = new Schedule();
        s.setId(rs.getLong("id"));
        s.setTourId(rs.getLong("tour_id"));
        Date start = rs.getDate("start_date");
        Date end = rs.getDate("end_date");
        if (start != null) {
            s.setStartDate(start.toLocalDate());
        }
        if (end != null) {
            s.setEndDate(end.toLocalDate());
        }
        return s;
    }
}
