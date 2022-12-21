package com.mauntung.mauntung.adapter.http.controller;

import com.mauntung.mauntung.application.exception.UserWithSpecifiedEmailExistedException;
import com.mauntung.mauntung.application.port.merchant.MerchantRegisterResponse;
import com.mauntung.mauntung.application.port.merchant.MerchantRegisterUseCase;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(AuthenticationController.class)
@AutoConfigureMockMvc(addFilters = false)
class AuthenticationControllerTest {
    @Autowired
    private MockMvc mvc;

    @MockBean
    private MerchantRegisterUseCase merchantRegisterUseCase;

    @Test
    void givenValidEmail_merchantRegister_shouldReturnCreatedStatus() throws Exception {
        String requestBody = "{\n" +
            "    \"merchantName\": \"KopiQue\",\n" +
            "    \"email\": \"kopique@merchant.com\",\n" +
            "    \"password\": \"password123\"\n" +
            "}";
        when(merchantRegisterUseCase.apply(any())).thenReturn(new MerchantRegisterResponse(1L, 1L, "KopiQue"));
        mvc.perform(
                post("/merchant/auth/register")
                    .content(requestBody)
                    .contentType(MediaType.APPLICATION_JSON)
            )
            .andExpect(status().isCreated())
            .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON));
    }

    @Test
    void givenMalformedEmail_merchantRegister_shouldReturnBadRequestStatus() throws Exception {
        String requestBody = "{\n" +
            "    \"merchantName\": \"KopiQue\",\n" +
            "    \"email\": \"kopique@merchant\",\n" +
            "    \"password\": \"password123\"\n" +
            "}";
        when(merchantRegisterUseCase.apply(any())).thenThrow(new IllegalArgumentException("Invalid email"));
        mvc.perform(
                post("/merchant/auth/register")
                    .content(requestBody)
                    .contentType(MediaType.APPLICATION_JSON)
            )
            .andExpect(status().isBadRequest())
            .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON));
    }

    @Test
    void givenExistedEmail_merchantRegister_shouldReturnBadRequestStatus() throws Exception {
        String requestBody = "{\n" +
            "    \"merchantName\": \"KopiQue\",\n" +
            "    \"email\": \"kopique@merchant.com\",\n" +
            "    \"password\": \"password123\"\n" +
            "}";
        when(merchantRegisterUseCase.apply(any())).thenThrow(new UserWithSpecifiedEmailExistedException());
        mvc.perform(
                post("/merchant/auth/register")
                    .content(requestBody)
                    .contentType(MediaType.APPLICATION_JSON)
            )
            .andExpect(status().isBadRequest())
            .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON));
    }
}