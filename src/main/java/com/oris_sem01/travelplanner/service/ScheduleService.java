package com.oris_sem01.travelplanner.service;

import com.oris_sem01.travelplanner.model.Schedule;
import com.oris_sem01.travelplanner.repository.ScheduleRepository;

import java.time.LocalDate;
import java.util.List;

public class ScheduleService {

    private final ScheduleRepository scheduleRepo;

    public ScheduleService(ScheduleRepository scheduleRepo) {
        this.scheduleRepo = scheduleRepo;
    }

    public Schedule createSchedule(Long tourId, LocalDate start, LocalDate end) {
        if (end.isBefore(start)) {
            throw new IllegalArgumentException("Дата окончания раньше даты начала");
        }
        Schedule s = new Schedule();
        s.setTourId(tourId);
        s.setStartDate(start);
        s.setEndDate(end);
        return scheduleRepo.save(s);
    }

    public List<Schedule> listByTour(Long tourId) {
        return scheduleRepo.findByTourId(tourId);
    }
}
