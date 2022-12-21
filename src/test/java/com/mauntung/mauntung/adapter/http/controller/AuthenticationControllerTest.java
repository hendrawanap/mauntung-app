package com.mauntung.mauntung.adapter.http.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mauntung.mauntung.application.exception.UserWithSpecifiedEmailExistedException;
import com.mauntung.mauntung.application.port.customer.CustomerRegisterResponse;
import com.mauntung.mauntung.application.port.customer.CustomerRegisterUseCase;
import com.mauntung.mauntung.application.port.merchant.MerchantRegisterResponse;
import com.mauntung.mauntung.application.port.merchant.MerchantRegisterUseCase;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Map;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(AuthenticationController.class)
@AutoConfigureMockMvc(addFilters = false)
class AuthenticationControllerTest {
    private static final String MERCHANT_REGISTER_URL = "/merchant/auth/register";
    private static final String CUSTOMER_REGISTER_URL = "/customer/auth/register";
    private static final ObjectMapper jsonMapper = new ObjectMapper();

    @Autowired
    private MockMvc mvc;

    @MockBean
    private MerchantRegisterUseCase merchantRegisterUseCase;

    @MockBean
    private CustomerRegisterUseCase customerRegisterUseCase;

    private static String prepareMerchantRegisterRequestBody(String merchantName, String email, String password) throws JsonProcessingException {
        return jsonMapper.writeValueAsString(Map.of(
            "merchantName", merchantName,
            "email", email,
            "password", password
        ));
    }

    private static String prepareCustomerRegisterRequestBody(String fullName, String email, String password) throws JsonProcessingException {
        return jsonMapper.writeValueAsString(Map.of(
            "fullName", fullName,
            "email", email,
            "password", password
        ));
    }

    @Test
    void givenValidEmail_merchantRegister_shouldReturnCreatedStatus() throws Exception {
        String merchantName = "KopiQue";
        String email = "kopique@merchant.com";
        String password = "password123";
        String requestBody = prepareMerchantRegisterRequestBody(merchantName, email, password);

        when(merchantRegisterUseCase.apply(any())).thenReturn(new MerchantRegisterResponse(1L, 1L, "KopiQue"));

        mvc.perform(
                post(MERCHANT_REGISTER_URL)
                    .content(requestBody)
                    .contentType(MediaType.APPLICATION_JSON)
            )
            .andExpect(status().isCreated())
            .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON));
    }

    @Test
    void givenMalformedEmail_merchantRegister_shouldReturnBadRequestStatus() throws Exception {
        String merchantName = "KopiQue";
        String email = "kopique";
        String password = "password123";
        String requestBody = prepareMerchantRegisterRequestBody(merchantName, email, password);

        when(merchantRegisterUseCase.apply(any())).thenThrow(new IllegalArgumentException("Invalid email"));

        mvc.perform(
                post(MERCHANT_REGISTER_URL)
                    .content(requestBody)
                    .contentType(MediaType.APPLICATION_JSON)
            )
            .andExpect(status().isBadRequest())
            .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON));
    }

    @Test
    void givenExistedEmail_merchantRegister_shouldReturnBadRequestStatus() throws Exception {
        String merchantName = "KopiQue";
        String email = "kopique@merchant.com";
        String password = "password123";
        String requestBody = prepareMerchantRegisterRequestBody(merchantName, email, password);

        when(merchantRegisterUseCase.apply(any())).thenThrow(new UserWithSpecifiedEmailExistedException());

        mvc.perform(
                post(MERCHANT_REGISTER_URL)
                    .content(requestBody)
                    .contentType(MediaType.APPLICATION_JSON)
            )
            .andExpect(status().isBadRequest())
            .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON));
    }

    @Test
    void givenValidEmail_customerRegister_shouldReturnCreatedStatus() throws Exception {
        String fullName = "Joomla";
        String email = "joomla@customer.com";
        String password = "password123";
        String requestBody = prepareCustomerRegisterRequestBody(fullName, email, password);

        when(customerRegisterUseCase.apply(any())).thenReturn(new CustomerRegisterResponse(1L, 1L, "Joomla"));

        mvc.perform(
                post(CUSTOMER_REGISTER_URL)
                    .content(requestBody)
                    .contentType(MediaType.APPLICATION_JSON)
            )
            .andExpect(status().isCreated())
            .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON));
    }

    @Test
    void givenMalformedEmail_customerRegister_shouldReturnBadRequestStatus() throws Exception {
        String fullName = "Joomla";
        String email = "joomla";
        String password = "password123";
        String requestBody = prepareCustomerRegisterRequestBody(fullName, email, password);

        when(customerRegisterUseCase.apply(any())).thenThrow(new IllegalArgumentException("Invalid email"));

        mvc.perform(
                post(CUSTOMER_REGISTER_URL)
                    .content(requestBody)
                    .contentType(MediaType.APPLICATION_JSON)
            )
            .andExpect(status().isBadRequest())
            .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON));
    }

    @Test
    void givenExistedEmail_customerRegister_shouldReturnBadRequestStatus() throws Exception {
        String fullName = "Joomla";
        String email = "joomla@customer.com";
        String password = "password123";
        String requestBody = prepareCustomerRegisterRequestBody(fullName, email, password);

        when(customerRegisterUseCase.apply(any())).thenThrow(new UserWithSpecifiedEmailExistedException());

        mvc.perform(
                post(CUSTOMER_REGISTER_URL)
                    .content(requestBody)
                    .contentType(MediaType.APPLICATION_JSON)
            )
            .andExpect(status().isBadRequest())
            .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON));
    }
}