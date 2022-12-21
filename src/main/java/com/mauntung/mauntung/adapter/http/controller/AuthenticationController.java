package com.mauntung.mauntung.adapter.http.controller;

import com.mauntung.mauntung.adapter.http.request.CustomerRegisterRequest;
import com.mauntung.mauntung.adapter.http.request.MerchantRegisterRequest;
import com.mauntung.mauntung.adapter.http.response.CustomerRegisterResponseBody;
import com.mauntung.mauntung.adapter.http.response.MerchantRegisterResponseBody;
import com.mauntung.mauntung.application.port.customer.CustomerRegisterCommand;
import com.mauntung.mauntung.application.port.customer.CustomerRegisterResponse;
import com.mauntung.mauntung.application.port.customer.CustomerRegisterUseCase;
import com.mauntung.mauntung.application.port.merchant.MerchantRegisterCommand;
import com.mauntung.mauntung.application.port.merchant.MerchantRegisterResponse;
import com.mauntung.mauntung.application.port.merchant.MerchantRegisterUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
public class AuthenticationController {
    private final MerchantRegisterUseCase merchantRegisterUseCase;
    private final CustomerRegisterUseCase customerRegisterUseCase;

    @PostMapping("/merchant/auth/register")
    public ResponseEntity<MerchantRegisterResponseBody> merchantRegister(@RequestBody @Valid MerchantRegisterRequest request) {
        MerchantRegisterCommand command = new MerchantRegisterCommand(
            request.getEmail(),
            request.getPassword(),
            request.getMerchantName()
        );
        MerchantRegisterResponse response = merchantRegisterUseCase.apply(command);
        MerchantRegisterResponseBody responseBody = new MerchantRegisterResponseBody(response);
        return new ResponseEntity<>(responseBody, HttpStatus.CREATED);
    }

    @PostMapping("/customer/auth/register")
    public ResponseEntity<CustomerRegisterResponseBody> customerRegister(@RequestBody @Valid CustomerRegisterRequest request) {
        CustomerRegisterCommand command = new CustomerRegisterCommand(
            request.getEmail(),
            request.getPassword(),
            request.getFullName()
        );
        CustomerRegisterResponse response = customerRegisterUseCase.apply(command);
        CustomerRegisterResponseBody responseBody = new CustomerRegisterResponseBody(response);
        return new ResponseEntity<>(responseBody, HttpStatus.CREATED);
    }
}
