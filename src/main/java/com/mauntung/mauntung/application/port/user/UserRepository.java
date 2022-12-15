package com.mauntung.mauntung.application.port.user;

import com.mauntung.mauntung.domain.model.user.User;

import java.util.Optional;

public interface UserRepository {
    Optional<User> findByEmail(String email);

    Optional<Long> save(User user);
}
