package com.mauntung.mauntung.domain.model.user;

import java.util.Date;

public interface UserFactory {
    User createWithId(String email, String password, String role, Date createdAt, Long id) throws IllegalStateException;

    User createWithoutId(String email, String password, String role, Date createdAt) throws IllegalStateException;
}
