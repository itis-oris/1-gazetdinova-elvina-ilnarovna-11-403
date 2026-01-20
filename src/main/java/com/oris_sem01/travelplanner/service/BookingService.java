package com.oris_sem01.travelplanner.service;

import com.oris_sem01.travelplanner.model.Booking;
import com.oris_sem01.travelplanner.repository.BookingRepository;

import java.util.List;
import java.util.Objects;

public class BookingService {

    private final BookingRepository bookingRepository;

    public BookingService(BookingRepository bookingRepository) {
        this.bookingRepository = Objects.requireNonNull(bookingRepository);
    }

    public List<Booking> findAll() {
        return bookingRepository.findAll();
    }

    public List<Booking> findByUserId(Long userId) {
        if (userId == null) {
            throw new IllegalArgumentException("userId не может быть null");
        }
        return bookingRepository.findByUserId(userId);
    }

    public List<Booking> findByTourId(Long tourId) {
        if (tourId == null) {
            throw new IllegalArgumentException("tourId не может быть null");
        }
        return bookingRepository.findByTourId(tourId);
    }

    /**
     ! Создаёт новое бронирование с проверкой на дубликаты
     */
    public void create(Long userId, Long tourId) {
        if (userId == null || tourId == null) {
            throw new IllegalArgumentException("userId и tourId обязательны");
        }

        // ПРОВЕРКА НА ДУБЛИКАТ
        List<Booking> existing = bookingRepository.findByUserId(userId);
        for (Booking b : existing) {
            if (b.getTourId().equals(tourId)) {
                throw new IllegalArgumentException("Ты уже забронировал(а) этот тур!");
            }
        }

        Booking booking = new Booking();
        booking.setUserId(userId);
        booking.setTourId(tourId);
        bookingRepository.create(booking);
    }

    public void delete(Long bookingId) {
        if (bookingId == null) {
            throw new IllegalArgumentException("bookingId не может быть null");
        }
        bookingRepository.delete(bookingId);
    }
}
