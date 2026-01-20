package com.oris_sem01.travelplanner.repository.jdbc;

import com.oris_sem01.travelplanner.model.Booking;
import com.oris_sem01.travelplanner.repository.BookingRepository;
import com.oris_sem01.travelplanner.util.ConnectionPool;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class JdbcBookingRepository implements BookingRepository {

    @Override
    public List<Booking> findAll() {
        String sql = """
                SELECT id, user_id, tour_id
                FROM bookings
                ORDER BY id DESC
                """;

        List<Booking> result = new ArrayList<>();

        try (Connection conn = ConnectionPool.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                result.add(mapRow(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Ошибка при получении списка бронирований", e);
        }

        return result;
    }

    @Override
    public Optional<Booking> findById(Long id) {
        String sql = """
                SELECT id, user_id, tour_id
                FROM bookings
                WHERE id = ?
                """;

        try (Connection conn = ConnectionPool.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setLong(1, id);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapRow(rs));
                } else {
                    return Optional.empty();
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException("Ошибка при поиске бронирования id=" + id, e);
        }
    }

    @Override
    public void create(Booking booking) {
        // Вставляем только user_id и tour_id — под текущую схему БД
        String sql = """
                INSERT INTO bookings (user_id, tour_id)
                VALUES (?, ?)
                """;

        try (Connection conn = ConnectionPool.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setLong(1, booking.getUserId());
            ps.setLong(2, booking.getTourId());

            ps.executeUpdate();

            try (ResultSet keys = ps.getGeneratedKeys()) {
                if (keys.next()) {
                    long generatedId = keys.getLong(1);
                    booking.setId(generatedId);
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException("Ошибка при создании бронирования", e);
        }
    }

    @Override
    public void update(Booking booking) {
        // Минимальное обновление: меняем только user_id и tour_id
        String sql = """
                UPDATE bookings
                SET user_id = ?, tour_id = ?
                WHERE id = ?
                """;

        try (Connection conn = ConnectionPool.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setLong(1, booking.getUserId());
            ps.setLong(2, booking.getTourId());
            ps.setLong(3, booking.getId());

            ps.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException("Ошибка при обновлении бронирования id=" + booking.getId(), e);
        }
    }

    @Override
    public void delete(Long id) {
        String sql = "DELETE FROM bookings WHERE id = ?";

        try (Connection conn = ConnectionPool.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setLong(1, id);
            ps.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException("Ошибка при удалении бронирования id=" + id, e);
        }
    }

    @Override
    public List<Booking> findByUserId(Long userId) {
        String sql = """
                SELECT id, user_id, tour_id
                FROM bookings
                WHERE user_id = ?
                ORDER BY id DESC
                """;

        List<Booking> result = new ArrayList<>();

        try (Connection conn = ConnectionPool.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setLong(1, userId);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    result.add(mapRow(rs));
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException("Ошибка при получении бронирований пользователя id=" + userId, e);
        }

        return result;
    }

    @Override
    public List<Booking> findByTourId(Long tourId) {
        String sql = """
                SELECT id, user_id, tour_id
                FROM bookings
                WHERE tour_id = ?
                ORDER BY id DESC
                """;

        List<Booking> result = new ArrayList<>();

        try (Connection conn = ConnectionPool.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setLong(1, tourId);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    result.add(mapRow(rs));
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException("Ошибка при получении бронирований по туру id=" + tourId, e);
        }

        return result;
    }

    /**
     * Маппинг строки ResultSet в Booking.
     * Заполняем только то, что достали из БД: id, userId, tourId.
     * Остальные поля (bookingDate, status, createdAt) в модели могут
     * существовать, но мы их просто не трогаем — они останутся null.
     */
    private Booking mapRow(ResultSet rs) throws SQLException {
        Booking b = new Booking();
        b.setId(rs.getLong("id"));
        b.setUserId(rs.getLong("user_id"));
        b.setTourId(rs.getLong("tour_id"));
        return b;
    }
}
