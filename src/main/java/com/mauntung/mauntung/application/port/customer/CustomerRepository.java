package com.mauntung.mauntung.application.port.customer;

import com.mauntung.mauntung.domain.model.customer.Customer;

import java.util.Optional;

public interface CustomerRepository {
    Optional<Customer> findByUserId(long userId);

    Optional<Long> save(Customer customer);

    Optional<Long> save(Customer customer, Long userId);
}
