package com.oris_sem01.travelplanner.service;

import com.oris_sem01.travelplanner.model.Review;
import com.oris_sem01.travelplanner.repository.ReviewRepository;

import java.util.List;

public class ReviewService {

    private final ReviewRepository reviewRepo;

    public ReviewService(ReviewRepository reviewRepo) {
        this.reviewRepo = reviewRepo;
    }

    public Review addReview(Long userId, Long tourId, int rating, String comment) {
        if (rating < 1 || rating > 5) {
            throw new IllegalArgumentException("Рейтинг должен быть от 1 до 5");
        }
        if (comment == null || comment.isBlank()) {
            throw new IllegalArgumentException("Комментарий обязателен");
        }

        Review r = new Review();
        r.setUserId(userId);
        r.setTourId(tourId);
        r.setRating(rating);
        r.setComment(comment);

        return reviewRepo.save(r);
    }

    public List<Review> listByTour(Long tourId) {
        return reviewRepo.findByTourId(tourId);
    }

    public List<Review> findByTourId(Long id) {
        return List.of();
    }
}
