package com.mauntung.mauntung.application.service;

import com.mauntung.mauntung.application.exception.UserWithSpecifiedEmailExistedException;
import com.mauntung.mauntung.application.port.customer.CustomerRegisterCommand;
import com.mauntung.mauntung.application.port.customer.CustomerRepository;
import com.mauntung.mauntung.application.port.user.UserRepository;
import com.mauntung.mauntung.domain.model.customer.Customer;
import com.mauntung.mauntung.domain.model.user.User;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CustomerRegisterServiceTest {
    private static UserRepository userRepository;
    private static CustomerRepository customerRepository;
    private static User user;
    private static Customer customer;
    private static String email;
    private static String password;
    private static String fullName;

    @BeforeAll
    static void beforeAll() {
        userRepository = mock(UserRepository.class);
        customerRepository = mock(CustomerRepository.class);
        user = mock(User.class);
        customer = mock(Customer.class);
        email = "customer1@mail.com";
        password = "password123";
        fullName = "Bambang";
    }
    @Test
    void givenExistedEmail_apply_shouldThrowsException() {
        when(userRepository.findByEmail(email)).thenReturn(Optional.of(user));

        CustomerRegisterService service = new CustomerRegisterService(userRepository, customerRepository);
        CustomerRegisterCommand command = new CustomerRegisterCommand(email, password, fullName);

        assertThrows(UserWithSpecifiedEmailExistedException.class, () -> service.apply(command));
    }

    @Test
    void givenNonExistedEmail_apply_shouldReturnResponse() {
        long userId = 1;
        long customerId = 1;

        when(customer.getName()).thenReturn(fullName);
        when(userRepository.findByEmail(email)).thenReturn(Optional.empty());
        when(userRepository.save(any())).thenReturn(Optional.of(userId));
        when(customerRepository.save(any(), any())).thenReturn(Optional.of(customerId));

        CustomerRegisterService service = new CustomerRegisterService(userRepository, customerRepository);
        CustomerRegisterCommand command = new CustomerRegisterCommand(email, password, fullName);

        assertNotNull(service.apply(command));
    }
}