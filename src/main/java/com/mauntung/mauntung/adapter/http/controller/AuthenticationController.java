package com.mauntung.mauntung.adapter.http.controller;

import com.mauntung.mauntung.adapter.http.request.MerchantRegisterRequest;
import com.mauntung.mauntung.adapter.http.response.MerchantRegisterResponseBody;
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
}
