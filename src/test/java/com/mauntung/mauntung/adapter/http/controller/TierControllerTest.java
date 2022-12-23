package com.mauntung.mauntung.adapter.http.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mauntung.mauntung.application.port.tier.CreateTierResponse;
import com.mauntung.mauntung.application.port.tier.CreateTierUseCase;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.stream.Stream;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest({ TierController.class })
@AutoConfigureMockMvc(addFilters = false)
class TierControllerTest {
    private static final String MERCHANT_TIERS_URL = "/merchant/tiers";
    private static final ObjectMapper jsonMapper = new ObjectMapper();

    @Autowired
    private MockMvc mvc;

    @MockBean
    private CreateTierUseCase createTierUseCase;

    private static String buildCreateTierRequestBody(String name, Set<Long> rewardIds, Integer requiredPoints, Float multiplierFactor) throws JsonProcessingException {
        Map<String, Object> requestMap = new HashMap<>(Map.of(
            "name", name,
            "rewardIds", rewardIds,
            "requiredPoints", requiredPoints
        ));

        if (multiplierFactor != null) requestMap.put("multiplierFactor", multiplierFactor);

        return jsonMapper.writeValueAsString(requestMap);
    }

    private static Stream<String> validCreateTierRequestBodyProvider() throws JsonProcessingException {
        String name = "name";
        Set<Long> rewardIds = Set.of(1L, 2L, 3L);
        int requiredPoints = 10;
        float multiplierFactor = 10;

        return Stream.of(
            buildCreateTierRequestBody(name, rewardIds, requiredPoints, null),
            buildCreateTierRequestBody(name, rewardIds, requiredPoints, multiplierFactor)
        );
    }

    private static Stream<String> invalidCreateTierRequestBodyProvider() throws JsonProcessingException {
        String name = "name";
        Set<Long> rewardIds = Set.of(1L, 2L, 3L);
        int requiredPoints = 10;
        float multiplierFactor = 10;

        return Stream.of(
            buildCreateTierRequestBody(name, rewardIds, -1, multiplierFactor),
            buildCreateTierRequestBody(name, rewardIds, requiredPoints, -1F)
        );
    }

    @ParameterizedTest
    @MethodSource("validCreateTierRequestBodyProvider")
    void givenValidArgs_createTier_shouldReturnCreatedStatus(String requestBody) throws Exception {
        when(createTierUseCase.apply(any())).thenReturn(new CreateTierResponse(1L, "name", 3, 10, 10F));

        mvc.perform(
            post(MERCHANT_TIERS_URL)
                .content(requestBody)
                .contentType(MediaType.APPLICATION_JSON)
        )
            .andExpect(status().isCreated())
            .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON));
    }

    @ParameterizedTest
    @MethodSource("invalidCreateTierRequestBodyProvider")
    void givenInvalidArgs_createTier_shouldReturnBadRequestStatus(String requestBody) throws Exception {
        mvc.perform(
                post(MERCHANT_TIERS_URL)
                    .content(requestBody)
                    .contentType(MediaType.APPLICATION_JSON)
            )
            .andExpect(status().isBadRequest())
            .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON));
    }
}