package com.mauntung.mauntung.application.service;

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
import java.util.Optional;
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
        CustomerRegisterResponse response = new CustomerRegisterResponse();

        boolean userIsExist = userIsExist(command.getEmail());
        if (userIsExist) {
            response.setErrorResponse("User with specified email is already exist");
            return response;
        }

        User newUser;
        try {
            newUser = createUser(command.getEmail(), command.getPassword());
        } catch (IllegalArgumentException ex) {
            response.setErrorResponse(ex.getMessage());
            return response;
        }

        Optional<Long> userId = userRepository.save(newUser);
        if (userId.isEmpty()) {
            response.setErrorResponse("Failed to create user");
            return response;
        }

        Customer newCustomer;
        try {
            newCustomer = createCustomer(command.getFullName());
        } catch (IllegalArgumentException ex) {
            response.setErrorResponse(ex.getMessage());
            return response;
        }

        Optional<Long> customerId = customerRepository.save(newCustomer, userId.get());
        if (customerId.isEmpty()) {
            response.setErrorResponse("Failed to create customer");
            return response;
        }

        response.setSuccessResponse(new CustomerRegisterResponse.SuccessResponse(
            userId.get(),
            customerId.get(),
            newCustomer.getName()
        ));
        return response;
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
