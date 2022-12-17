package com.mauntung.mauntung.application.service;

import com.mauntung.mauntung.application.port.customer.CustomerRegisterCommand;
import com.mauntung.mauntung.application.port.customer.CustomerRegisterResponse;
import com.mauntung.mauntung.application.port.customer.CustomerRepository;
import com.mauntung.mauntung.application.port.user.UserRepository;
import com.mauntung.mauntung.domain.model.customer.Customer;
import com.mauntung.mauntung.domain.model.user.User;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CustomerRegisterServiceTest {
    @Test
    void givenExistedEmail_apply_shouldReturnFailedResponse() {
        String email = "customer1@mail.com";
        String password = "password123";
        String fullName = "Bambang";

        User user = mock(User.class);

        UserRepository userRepository = mock(UserRepository.class);
        when(userRepository.findByEmail(email)).thenReturn(Optional.of(user));

        CustomerRepository customerRepository = mock(CustomerRepository.class);

        // Testing start
        CustomerRegisterService service = new CustomerRegisterService(userRepository, customerRepository);
        CustomerRegisterCommand command = new CustomerRegisterCommand(email, password, fullName);
        CustomerRegisterResponse response = service.apply(command);

        assertNotNull(response.getErrorResponse());
        assertNull(response.getSuccessResponse());
    }

    @Test
    void givenNonExistedEmail_apply_shouldReturnSuccessResponse() {
        String email = "customer1@mail.com";
        String password = "password123";
        String fullName = "Bambang";

        long userId = 1;

        Customer customer = mock(Customer.class);
        long customerId = 1;
        when(customer.getName()).thenReturn(fullName);

        UserRepository userRepository = mock(UserRepository.class);
        when(userRepository.findByEmail(email)).thenReturn(Optional.empty());
        when(userRepository.save(any())).thenReturn(Optional.of(userId));

        CustomerRepository customerRepository = mock(CustomerRepository.class);
        when(customerRepository.save(any())).thenReturn(Optional.of(customerId));

        // Testing start
        CustomerRegisterService service = new CustomerRegisterService(userRepository, customerRepository);
        CustomerRegisterCommand command = new CustomerRegisterCommand(email, password, fullName);
        CustomerRegisterResponse response = service.apply(command);

        assertNull(response.getErrorResponse());
        assertNotNull(response.getSuccessResponse());
    }
}