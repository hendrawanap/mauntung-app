package com.mauntung.mauntung.application.port.customer;

public interface CustomerRegisterUseCase {
    CustomerRegisterResponse apply(CustomerRegisterCommand command);
}
