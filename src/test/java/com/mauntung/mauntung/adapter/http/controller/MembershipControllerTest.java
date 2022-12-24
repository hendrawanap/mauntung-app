package com.mauntung.mauntung.adapter.http.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mauntung.mauntung.adapter.http.security.JwtTokenService;
import com.mauntung.mauntung.application.port.membership.CreatePointMembershipResponse;
import com.mauntung.mauntung.application.port.membership.CreatePointMembershipUseCase;
import com.mauntung.mauntung.application.port.membership.CreateStampMembershipResponse;
import com.mauntung.mauntung.application.port.membership.CreateStampMembershipUseCase;
import com.mauntung.mauntung.domain.model.membership.PointGeneration;
import com.mauntung.mauntung.domain.model.membership.PointRules;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.*;

@WebMvcTest({ MembershipController.class })
class MembershipControllerTest {
    private static final String MERCHANT_MEMBERSHIP_URL = "/merchant/membership";
    private static final ObjectMapper jsonMapper = new ObjectMapper();

    @Autowired
    private MockMvc mvc;

    @MockBean
    private CreatePointMembershipUseCase createPointMembershipUseCase;

    @MockBean
    private CreateStampMembershipUseCase createStampMembershipUseCase;

    @MockBean
    private JwtTokenService jwtTokenService;

    private static String buildInvalidTypeCreateMembershipRequestBody() throws JsonProcessingException {
        return jsonMapper.writeValueAsString(Map.of(
           "name", "name",
           "type", "invalid",
            "rules", Map.of(
                "stamp", Map.of(
                    "stampTtl", 10,
                    "redeemTtl", 10,
                    "cardCapacity", 10
                )
            ),
            "rewardIds", Set.of(1, 2, 3)
        ));
    }

    private static String buildCreatePointMembershipRequestBody(String name, String type, PointRules rules, Set<Long> rewardIds, Set<Long> tierIds, Long mediaId) throws JsonProcessingException {
        Map<String, Object> requestMap = new HashMap<>(Map.of(
            "name", name,
            "type", type,
            "rules", Map.of(
                "point", rules
            ),
            "rewardIds", rewardIds,
            "tierIds", tierIds
        ));

        if (mediaId != null) requestMap.put("mediaId", mediaId);

        return jsonMapper.writeValueAsString(requestMap);
    }

    private static String buildCreateStampMembershipRequestBody(String name, String type, Integer stampTtl, Integer redeemTtl, Integer cardCapacity, Set<Long> rewardIds, Long mediaId) throws JsonProcessingException {
        Map<String, Object> requestMap = new HashMap<>(Map.of(
            "name", name,
            "type", type,
            "rules", Map.of(
                "stamp", Map.of(
                    "stampTtl", stampTtl,
                    "redeemTtl", redeemTtl,
                    "cardCapacity", cardCapacity
                )
            ),
            "rewardIds", rewardIds
        ));

        if (mediaId != null) requestMap.put("mediaId", mediaId);

        return jsonMapper.writeValueAsString(requestMap);
    }

    @Test
    void givenInvalidType_createMembership_shouldReturnBadRequestStatus() throws Exception {
        String requestBody = buildInvalidTypeCreateMembershipRequestBody();

        when(jwtTokenService.getUserId(any())).thenReturn(1L);

        mvc.perform(
            post(MERCHANT_MEMBERSHIP_URL)
                .content(requestBody)
                .contentType(MediaType.APPLICATION_JSON)
                .with(jwt())
        )
            .andExpect(status().isBadRequest())
            .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON));
    }

    @Test
    void givenValidPointMembership_createMembership_shouldReturnCreatedStatus() throws Exception {
        String name = "name";
        String type = "point";
        PointRules rules = new PointRules(
            10,
            10,
            10,
            PointRules.DistributionMethod.POINT_CODE_GENERATION,
            Set.of(PointRules.RewardClaimMethod.BY_CUSTOMER),
            new PointGeneration(PointGeneration.Type.NOMINAL, 10, 10_000)
        );
        Set<Long> rewardIds = Set.of(1L, 2L, 3L);
        Set<Long> tierIds = Set.of(1L, 2L, 3L);
        String requestBody = buildCreatePointMembershipRequestBody(name, type, rules, rewardIds, tierIds, null);

        when(jwtTokenService.getUserId(any())).thenReturn(1L);
        when(createPointMembershipUseCase.apply(any())).thenReturn(new CreatePointMembershipResponse(1L, name, rewardIds.size(), tierIds.size(), new Date()));

        mvc.perform(
                post(MERCHANT_MEMBERSHIP_URL)
                    .content(requestBody)
                    .contentType(MediaType.APPLICATION_JSON)
                    .with(jwt())
            )
            .andExpect(status().isCreated())
            .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON));
    }

    @Test
    void givenValidStampMembership_createMembership_shouldReturnCreatedStatus() throws Exception {
        String name = "name";
        String type = "stamp";
        int stampTtl = 10;
        int redeemTtl = 10;
        int cardCapacity = 10;
        Set<Long> rewardIds = Set.of(1L, 2L, 3L);
        String requestBody = buildCreateStampMembershipRequestBody(name, type, stampTtl, redeemTtl, cardCapacity, rewardIds, null);

        when(jwtTokenService.getUserId(any())).thenReturn(1L);
        when(createStampMembershipUseCase.apply(any())).thenReturn(new CreateStampMembershipResponse(1L, name, rewardIds.size(), cardCapacity, new Date()));

        mvc.perform(
                post(MERCHANT_MEMBERSHIP_URL)
                    .content(requestBody)
                    .contentType(MediaType.APPLICATION_JSON)
                    .with(jwt())
            )
            .andExpect(status().isCreated())
            .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON));
    }
}