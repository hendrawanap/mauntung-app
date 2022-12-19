package com.mauntung.mauntung.adapter.persistence.customer;

import com.mauntung.mauntung.application.port.customer.CustomerRepository;
import com.mauntung.mauntung.domain.model.customer.Customer;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;


@Component
@RequiredArgsConstructor
public class CustomerRepositoryAdapter implements CustomerRepository {
    private final JpaCustomerRepository jpaRepository;
    private final CustomerMapper mapper = new CustomerMapper();

    @Override
    public Optional<Customer> findByUserId(long userId) {
        Optional<CustomerEntity> entity = jpaRepository.findByUserId(userId);
        if (entity.isEmpty()) return Optional.empty();
        return Optional.of(mapper.entityToModel(entity.get()));
    }

    @Override
    public Optional<Long> save(Customer customer) {
        CustomerEntity entity = mapper.modelToEntity(customer);
        try {
            entity = jpaRepository.save(entity);
        } catch (IllegalStateException ex) {
            return Optional.empty();
        }
        return Optional.of(entity.getId());
    }

    @Override
    public Optional<Long> save(Customer customer, Long userId) {
        CustomerEntity entity = mapper.modelToEntity(customer, userId);
        try {
            entity = jpaRepository.save(entity);
        } catch (IllegalStateException ex) {
            return Optional.empty();
        }
        return Optional.of(entity.getId());
    }
}
