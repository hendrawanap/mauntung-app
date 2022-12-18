package com.mauntung.mauntung.adapter.persistence.customer;

import com.mauntung.mauntung.adapter.persistence.user.UserEntity;
import com.mauntung.mauntung.application.port.customer.CustomerRepository;
import com.mauntung.mauntung.domain.model.customer.Customer;
import com.mauntung.mauntung.domain.model.customer.CustomerFactory;
import com.mauntung.mauntung.domain.model.customer.CustomerFactoryImpl;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.util.Date;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class CustomerRepositoryImplTest {
    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private JpaCustomerRepository jpaCustomerRepository;

    @Test
    void findByUserId_shouldReturnCustomer() {
        String email = "customer@mail.com";
        UserEntity userEntity = UserEntity.builder()
            .email(email)
            .role("customer")
            .password("password123")
            .build();
        Long userId = entityManager.persist(userEntity).getId();
        CustomerEntity customerEntity = CustomerEntity.builder()
            .name("customer")
            .customerCode(UUID.randomUUID())
            .user(UserEntity.builder().id(userId).build())
            .build();
        entityManager.persist(customerEntity);
        entityManager.flush();

        CustomerRepository customerRepository = new CustomerRepositoryImpl(jpaCustomerRepository);

        Optional<Customer> customer = customerRepository.findByUserId(userId);
        assertTrue(customer.isPresent());
    }

    @Test
    void findByUserId_shouldReturnEmpty() {
        String email = "customer@mail.com";
        UserEntity userEntity = UserEntity.builder()
            .email(email)
            .role("customer")
            .password("password123")
            .build();
        Long userId = entityManager.persist(userEntity).getId();
        CustomerEntity customerEntity = CustomerEntity.builder()
            .name("customer")
            .customerCode(UUID.randomUUID())
            .user(UserEntity.builder().id(userId).build())
            .build();
        entityManager.persist(customerEntity);
        entityManager.flush();

        CustomerRepository customerRepository = new CustomerRepositoryImpl(jpaCustomerRepository);

        long differentUserId = userId != 1L ? 1L : 2L;
        Optional<Customer> customer = customerRepository.findByUserId(differentUserId);
        assertTrue(customer.isEmpty());
    }

    @Test
    void save_shouldReturnId() {
        String email = "customer@mail.com";
        UserEntity userEntity = UserEntity.builder()
            .email(email)
            .role("customer")
            .password("password123")
            .build();
        Long userId = entityManager.persist(userEntity).getId();
        CustomerEntity customerEntity = CustomerEntity.builder()
            .name("customer")
            .customerCode(UUID.randomUUID())
            .user(UserEntity.builder().id(userId).build())
            .build();
        entityManager.persist(customerEntity);
        entityManager.flush();

        CustomerRepository customerRepository = new CustomerRepositoryImpl(jpaCustomerRepository);
        CustomerFactory customerFactory = new CustomerFactoryImpl();
        Customer customer = customerFactory.builder("newName", customerEntity.getCustomerCode(), customerEntity.getCreatedAt())
            .id(customerEntity.getId())
            .build();

        Optional<Long> customerId = customerRepository.save(customer);
        assertTrue(customerId.isPresent());
    }

    @Test
    void givenUserId_save_shouldReturnId() {
        String email = "customer@mail.com";
        UserEntity userEntity = UserEntity.builder()
            .email(email)
            .role("customer")
            .password("password123")
            .build();
        Long userId = entityManager.persist(userEntity).getId();
        entityManager.flush();

        CustomerRepository customerRepository = new CustomerRepositoryImpl(jpaCustomerRepository);
        CustomerFactory customerFactory = new CustomerFactoryImpl();
        Customer customer = customerFactory.builder("newName", UUID.randomUUID(), new Date()).build();

        Optional<Long> customerId = customerRepository.save(customer, userId);
        assertTrue(customerId.isPresent());
    }
}