package com.mauntung.mauntung.adapter.persistence.user;

import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface JpaUserRepository extends CrudRepository<UserEntity, Long> {
    Optional<UserEntity> findByEmail(String email);
}
