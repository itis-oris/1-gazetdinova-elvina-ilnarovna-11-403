package com.oris_sem01.travelplanner.repository;

import com.oris_sem01.travelplanner.model.Booking;

import java.util.List;
import java.util.Optional;

public interface BookingRepository {

    List<Booking> findAll();

    Optional<Booking> findById(Long id);

    void create(Booking booking);

    void update(Booking booking);

    void delete(Long id);

    List<Booking> findByUserId(Long userId);

    List<Booking> findByTourId(Long tourId);
}
