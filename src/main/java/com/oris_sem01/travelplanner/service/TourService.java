package com.oris_sem01.travelplanner.service;

import com.oris_sem01.travelplanner.model.Tour;
import com.oris_sem01.travelplanner.repository.TourRepository;

import java.util.List;
import java.util.Optional;

public class TourService {

    private final TourRepository tourRepository;

    public TourService(TourRepository tourRepository) {
        this.tourRepository = tourRepository;
    }

    public List<Tour> listAll() {
        return tourRepository.findAll();
    }

    public Optional<Tour> findById(Long id) {
        if (id == null) return Optional.empty();
        return tourRepository.findById(id);
    }

    public void save(Tour tour) {
        if (tour.getId() == null) {
            tourRepository.create(tour);
        } else {
            tourRepository.update(tour);
        }
    }

    public void delete(Long id) {
        if (id == null) return;
        tourRepository.delete(id);
    }
}
