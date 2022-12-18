package com.mauntung.mauntung.adapter.persistence.customer;

import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface JpaCustomerRepository extends CrudRepository<CustomerEntity, Long> {
    Optional<CustomerEntity> findByUserId(Long userId);
}
