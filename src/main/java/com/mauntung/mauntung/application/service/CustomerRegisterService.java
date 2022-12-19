package com.mauntung.mauntung.application.service;

import com.mauntung.mauntung.application.exception.UserWithSpecifiedEmailExistedException;
import com.mauntung.mauntung.application.port.customer.CustomerRegisterCommand;
import com.mauntung.mauntung.application.port.customer.CustomerRegisterResponse;
import com.mauntung.mauntung.application.port.customer.CustomerRegisterUseCase;
import com.mauntung.mauntung.application.port.customer.CustomerRepository;
import com.mauntung.mauntung.application.port.user.UserRepository;
import com.mauntung.mauntung.domain.model.customer.Customer;
import com.mauntung.mauntung.domain.model.customer.CustomerFactory;
import com.mauntung.mauntung.domain.model.customer.CustomerFactoryImpl;
import com.mauntung.mauntung.domain.model.user.User;
import com.mauntung.mauntung.domain.model.user.UserFactory;
import com.mauntung.mauntung.domain.model.user.UserFactoryImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class CustomerRegisterService implements CustomerRegisterUseCase {
    private final UserRepository userRepository;
    private final CustomerRepository customerRepository;
    private final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
    private final UserFactory userFactory = new UserFactoryImpl();
    private final CustomerFactory customerFactory = new CustomerFactoryImpl();

    @Override
    public CustomerRegisterResponse apply(CustomerRegisterCommand command) {
        boolean userIsExist = userIsExist(command.getEmail());
        if (userIsExist)
            throw new UserWithSpecifiedEmailExistedException();

        User user = createUser(command.getEmail(), command.getPassword());
        Long userId = saveUserAndGetId(user);

        Customer customer = createCustomer(command.getFullName());
        Long customerId = saveCustomerAndGetId(customer, userId);

        return buildResponse(userId, customerId, command.getFullName());
    }

    private CustomerRegisterResponse buildResponse(long userId, long customerId, String customerFullName) {
        return new CustomerRegisterResponse(userId, customerId, customerFullName);
    }

    private Long saveCustomerAndGetId(Customer customer, Long userId) {
        return customerRepository.save(customer, userId).orElseThrow(() -> new RuntimeException("Failed to save customer"));
    }

    private Long saveUserAndGetId(User user) {
        return userRepository.save(user).orElseThrow(() -> new RuntimeException("Failed to save user"));
    }

    private boolean userIsExist(String email) {
        return userRepository.findByEmail(email).isPresent();
    }

    private User createUser(String email, String password) throws IllegalArgumentException {
        return userFactory.createWithoutId(email, passwordEncoder.encode(password), User.Role.CUSTOMER, new Date());
    }

    private Customer createCustomer(String fullName) throws IllegalArgumentException {
        return customerFactory.builder(
            fullName,
            UUID.randomUUID(),
            new Date()
        ).build();
    }
}
