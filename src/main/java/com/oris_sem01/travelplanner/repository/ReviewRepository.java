package com.oris_sem01.travelplanner.repository;

import com.oris_sem01.travelplanner.model.Review;

import java.util.List;
import java.util.Optional;

public interface ReviewRepository {
    Review save(Review review);
    Optional<Review> findById(Long id);
    List<Review> findByTourId(Long tourId);
    List<Review> findByUserId(Long userId);
}
