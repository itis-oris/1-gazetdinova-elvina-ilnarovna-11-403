package com.oris_sem01.travelplanner.repository;

import com.oris_sem01.travelplanner.model.User;

import java.util.List;
import java.util.Optional;

public interface UserRepository {
    Optional<User> findById(Long id);
    Optional<User> findByEmail(String email);
    List<User> findAll();
    User save(User user);
    boolean deleteById(Long id);
}
