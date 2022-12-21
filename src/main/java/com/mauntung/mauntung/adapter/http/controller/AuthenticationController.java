package com.mauntung.mauntung.adapter.http.controller;

import com.mauntung.mauntung.adapter.http.request.authentication.CustomerLoginRequest;
import com.mauntung.mauntung.adapter.http.request.authentication.CustomerRegisterRequest;
import com.mauntung.mauntung.adapter.http.request.authentication.MerchantLoginRequest;
import com.mauntung.mauntung.adapter.http.request.authentication.MerchantRegisterRequest;
import com.mauntung.mauntung.adapter.http.response.authentication.CustomerLoginResponseBody;
import com.mauntung.mauntung.adapter.http.response.authentication.CustomerRegisterResponseBody;
import com.mauntung.mauntung.adapter.http.response.authentication.MerchantLoginResponseBody;
import com.mauntung.mauntung.adapter.http.response.authentication.MerchantRegisterResponseBody;
import com.mauntung.mauntung.adapter.http.security.JwtTokenService;
import com.mauntung.mauntung.adapter.http.security.UserDetailsImpl;
import com.mauntung.mauntung.application.port.customer.CustomerRegisterCommand;
import com.mauntung.mauntung.application.port.customer.CustomerRegisterResponse;
import com.mauntung.mauntung.application.port.customer.CustomerRegisterUseCase;
import com.mauntung.mauntung.application.port.merchant.MerchantRegisterCommand;
import com.mauntung.mauntung.application.port.merchant.MerchantRegisterResponse;
import com.mauntung.mauntung.application.port.merchant.MerchantRegisterUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.Date;

@RestController
@RequiredArgsConstructor
public class AuthenticationController {
    private final MerchantRegisterUseCase merchantRegisterUseCase;
    private final CustomerRegisterUseCase customerRegisterUseCase;
    private final AuthenticationManager authenticationManager;
    private final JwtTokenService jwtTokenService;

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

    @PostMapping("/merchant/auth/login")
    public ResponseEntity<MerchantLoginResponseBody> merchantLogin(@RequestBody @Valid MerchantLoginRequest request) {
        Authentication auth = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));
        UserDetailsImpl userDetails = (UserDetailsImpl) auth.getPrincipal();
        String token = jwtTokenService.generateToken(auth);
        Date tokenExpiredDate = jwtTokenService.getTokenExpiredDate(token);
        MerchantLoginResponseBody responseBody = new MerchantLoginResponseBody(userDetails.getId(), token, tokenExpiredDate);
        return new ResponseEntity<>(responseBody, HttpStatus.OK);
    }

    @PostMapping("/customer/auth/login")
    public ResponseEntity<CustomerLoginResponseBody> customerLogin(@RequestBody @Valid CustomerLoginRequest request) {
        Authentication auth = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));
        UserDetailsImpl userDetails = (UserDetailsImpl) auth.getPrincipal();
        String token = jwtTokenService.generateToken(auth);
        Date tokenExpiredDate = jwtTokenService.getTokenExpiredDate(token);
        CustomerLoginResponseBody responseBody = new CustomerLoginResponseBody(userDetails.getId(), token, tokenExpiredDate);
        return new ResponseEntity<>(responseBody, HttpStatus.OK);
    }
}
