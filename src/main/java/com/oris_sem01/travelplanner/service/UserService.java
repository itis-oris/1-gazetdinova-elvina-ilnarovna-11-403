package com.oris_sem01.travelplanner.service;

import com.oris_sem01.travelplanner.model.Role;
import com.oris_sem01.travelplanner.model.User;
import com.oris_sem01.travelplanner.repository.UserRepository;
import com.oris_sem01.travelplanner.util.PasswordUtils;

import java.util.Optional;

public class UserService {

    private final UserRepository userRepo;

    public UserService(UserRepository userRepo) {
        this.userRepo = userRepo;
    }

    public Optional<User> login(String email, String password) {
        return userRepo.findByEmail(email)
                .filter(u -> PasswordUtils.verifyPassword(password, u.getPasswordHash()));
    }

    public User register(String email, String firstName, String lastName, String password) {
        if (email == null || email.isBlank()) {
            throw new IllegalArgumentException("Email обязателен");
        }
        if (password == null || password.length() < 6) {
            throw new IllegalArgumentException("Пароль слишком короткий");
        }
        if (userRepo.findByEmail(email).isPresent()) {
            throw new IllegalArgumentException("Пользователь с таким email уже существует");
        }

        User user = new User();
        user.setEmail(email.trim());
        user.setFirstName(firstName);
        user.setLastName(lastName);
        user.setPasswordHash(PasswordUtils.hashPassword(password));
        user.setRole(Role.USER);

        return userRepo.save(user);
    }
}
