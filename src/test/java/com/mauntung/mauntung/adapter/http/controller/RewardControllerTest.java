package com.mauntung.mauntung.adapter.http.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mauntung.mauntung.adapter.http.security.JwtTokenService;
import com.mauntung.mauntung.application.port.reward.CreateRewardResponse;
import com.mauntung.mauntung.application.port.reward.CreateRewardUseCase;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.*;


@WebMvcTest({ RewardController.class })
class RewardControllerTest {
    private static final String MERCHANT_REWARDS_URL = "/merchant/rewards";
    private static final ObjectMapper jsonMapper = new ObjectMapper();

    @Autowired
    private MockMvc mvc;

    @MockBean
    private CreateRewardUseCase createRewardUseCase;

    @MockBean
    private JwtTokenService jwtTokenService;

    private static String prepareCreateRewardRequestBody(String name, String description, String termsCondition, Integer cost, Integer stock, String startPeriod, String endPeriod) throws JsonProcessingException {
        Map<String, Object> requestMap = new HashMap<>(Map.of(
            "name", name,
            "description", description,
            "termsCondition", termsCondition,
            "cost", cost
        ));

        if (stock != null) requestMap.put("stock", stock);
        if (startPeriod != null) requestMap.put("startPeriod", startPeriod);
        if (endPeriod != null) requestMap.put("endPeriod", endPeriod);

        return jsonMapper.writeValueAsString(requestMap);
    }

    private static Stream<String> validCreateRewardRequestBodyProvider() throws JsonProcessingException {
        String name = "name";
        String description = "desc";
        String termsCondition = "terms";
        Integer cost = 10;
        Integer stock = 10;
        String startPeriod = "2022-12-01";
        String endPeriod = "2022-12-31";

        return Stream.of(
            prepareCreateRewardRequestBody(name, description, termsCondition, cost, null, null, null),
            prepareCreateRewardRequestBody(name, description, termsCondition, cost, stock, null, null),
            prepareCreateRewardRequestBody(name, description, termsCondition, cost, null, startPeriod, endPeriod),
            prepareCreateRewardRequestBody(name, description, termsCondition, cost, stock, startPeriod, endPeriod)
        );
    }

    private static Stream<String> invalidCreateRewardRequestBodyProvider() throws JsonProcessingException {
        String name = "name";
        String description = "desc";
        String termsCondition = "terms";
        Integer cost = 10;
        Integer stock = 10;
        String startPeriod = "2022-12-01";
        String endPeriod = "2022-12-31";

        return Stream.of(
            prepareCreateRewardRequestBody("", description, termsCondition, cost, stock, startPeriod, endPeriod),
            prepareCreateRewardRequestBody(name, "", termsCondition, cost, stock, startPeriod, endPeriod),
            prepareCreateRewardRequestBody(name, description, "", cost, stock, startPeriod, endPeriod),
            prepareCreateRewardRequestBody(name, description, termsCondition, -10, stock, startPeriod, endPeriod),
            prepareCreateRewardRequestBody(name, description, termsCondition, cost, -10, startPeriod, endPeriod),
            prepareCreateRewardRequestBody(name, description, termsCondition, cost, stock, null, endPeriod),
            prepareCreateRewardRequestBody(name, description, termsCondition, cost, stock, startPeriod, null),
            prepareCreateRewardRequestBody(name, description, termsCondition, cost, stock, "invalid", endPeriod),
            prepareCreateRewardRequestBody(name, description, termsCondition, cost, stock, startPeriod, "invalid")
        );
    }

    @ParameterizedTest
    @MethodSource("validCreateRewardRequestBodyProvider")
    void givenValidArgs_createReward_shouldReturnCreatedStatus(String requestBody) throws Exception {
        when(jwtTokenService.getUserId(any())).thenReturn(1L);
        when(createRewardUseCase.apply(any())).thenReturn(new CreateRewardResponse(1L, new Date(), "success"));

        mvc.perform(
            post(MERCHANT_REWARDS_URL)
                .content(requestBody)
                .contentType(MediaType.APPLICATION_JSON)
                .with(jwt())
        )
            .andExpect(status().isCreated())
            .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON));
    }

    @ParameterizedTest
    @MethodSource("invalidCreateRewardRequestBodyProvider")
    void givenInvalidArgs_createReward_shouldReturnBadRequestStatus(String requestBody) throws Exception {
        mvc.perform(
                post(MERCHANT_REWARDS_URL)
                    .content(requestBody)
                    .contentType(MediaType.APPLICATION_JSON)
                    .with(jwt())
            )
            .andExpect(status().isBadRequest())
            .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON));
    }
}