package com.oris_sem01.travelplanner.repository;

import com.oris_sem01.travelplanner.model.Schedule;

import java.util.List;
import java.util.Optional;

public interface ScheduleRepository {
    Schedule save(Schedule schedule);
    Optional<Schedule> findById(Long id);
    List<Schedule> findByTourId(Long tourId);
}
