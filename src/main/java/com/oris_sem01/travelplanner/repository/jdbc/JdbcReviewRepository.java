package com.oris_sem01.travelplanner.repository.jdbc;

import com.oris_sem01.travelplanner.config.DatabaseConfig;
import com.oris_sem01.travelplanner.model.Review;
import com.oris_sem01.travelplanner.repository.ReviewRepository;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class JdbcReviewRepository implements ReviewRepository {

    @Override
    public Review save(Review review) {
        if (review.getId() == null) {
            return insert(review);
        }
        return review;
    }

    private Review insert(Review review) {
        String sql = "INSERT INTO reviews (user_id, tour_id, rating, comment, created_at) " +
                "VALUES (?, ?, ?, ?, now()) RETURNING id, created_at";
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setLong(1, review.getUserId());
            ps.setLong(2, review.getTourId());
            ps.setInt(3, review.getRating());
            ps.setString(4, review.getComment());

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    review.setId(rs.getLong("id"));
                    review.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime());
                }
            }
            return review;
        } catch (SQLException e) {
            throw new RuntimeException("Ошибка при добавлении отзыва", e);
        }
    }

    @Override
    public Optional<Review> findById(Long id) {
        String sql = "SELECT id, user_id, tour_id, rating, comment, created_at FROM reviews WHERE id = ?";
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setLong(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapRow(rs));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Ошибка при поиске отзыва", e);
        }
        return Optional.empty();
    }

    @Override
    public List<Review> findByTourId(Long tourId) {
        String sql = "SELECT id, user_id, tour_id, rating, comment, created_at FROM reviews WHERE tour_id = ? ORDER BY created_at DESC";
        List<Review> result = new ArrayList<>();
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setLong(1, tourId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    result.add(mapRow(rs));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Ошибка при получении отзывов по туру", e);
        }
        return result;
    }

    @Override
    public List<Review> findByUserId(Long userId) {
        String sql = "SELECT id, user_id, tour_id, rating, comment, created_at FROM reviews WHERE user_id = ? ORDER BY created_at DESC";
        List<Review> result = new ArrayList<>();
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setLong(1, userId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    result.add(mapRow(rs));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Ошибка при получении отзывов по пользователю", e);
        }
        return result;
    }

    private Review mapRow(ResultSet rs) throws SQLException {
        Review r = new Review();
        r.setId(rs.getLong("id"));
        r.setUserId(rs.getLong("user_id"));
        r.setTourId(rs.getLong("tour_id"));
        r.setRating(rs.getInt("rating"));
        r.setComment(rs.getString("comment"));
        Timestamp ts = rs.getTimestamp("created_at");
        if (ts != null) {
            r.setCreatedAt(ts.toLocalDateTime());
        }
        return r;
    }
}
