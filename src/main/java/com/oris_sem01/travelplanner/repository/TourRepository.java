package com.oris_sem01.travelplanner.repository;

import com.oris_sem01.travelplanner.model.Tour;

import java.util.List;
import java.util.Optional;

public interface TourRepository {

    List<Tour> findAll();

    Optional<Tour> findById(Long id);

    void create(Tour tour);

    void update(Tour tour);

    void delete(Long id);
}
