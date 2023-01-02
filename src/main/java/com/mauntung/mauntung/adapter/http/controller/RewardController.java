package com.mauntung.mauntung.adapter.http.controller;

import com.mauntung.mauntung.adapter.http.request.reward.CreateRewardRequest;
import com.mauntung.mauntung.adapter.http.response.reward.CreateRewardResponseBody;
import com.mauntung.mauntung.adapter.http.response.reward.ListMembershipRewardsResponseBody;
import com.mauntung.mauntung.adapter.http.security.JwtTokenService;
import com.mauntung.mauntung.application.port.reward.*;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.PositiveOrZero;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

@RestController
@RequiredArgsConstructor
@Validated
public class RewardController {
    private final CreateRewardUseCase createRewardUseCase;
    private final ListMembershipRewardsUseCase listMembershipRewardsUseCase;
    private final JwtTokenService jwtTokenService;

    @PreAuthorize("hasRole('MERCHANT')")
    @PostMapping("merchant/rewards")
    public ResponseEntity<CreateRewardResponseBody> createReward(@RequestBody @Valid CreateRewardRequest request, Authentication authentication) {
        Long userId = getUserIdFromAuthentication(authentication);
        CreateRewardCommand command = buildCreateRewardCommand(request, userId);
        CreateRewardResponse response = createRewardUseCase.apply(command);
        return new ResponseEntity<>(new CreateRewardResponseBody(response), HttpStatus.CREATED);
    }

    @PreAuthorize("hasRole('MERCHANT')")
    @GetMapping("merchant/rewards")
    public ResponseEntity<ListMembershipRewardsResponseBody> listMembershipRewards(
        @RequestParam(required = false) @PositiveOrZero Integer maxCost,
        @RequestParam(required = false) @PositiveOrZero Integer minCost,
        @RequestParam(required = false, name = "q") String requestQuery,
        Authentication authentication
    ) {
        Long userId = getUserIdFromAuthentication(authentication);
        ListMembershipRewardsQuery query = buildListMembershipRewardsQuery(maxCost, minCost, requestQuery, userId);
        ListMembershipRewardsResponse response = listMembershipRewardsUseCase.apply(query);
        return ResponseEntity.ok(new ListMembershipRewardsResponseBody(response));
    }

    private CreateRewardCommand buildCreateRewardCommand(CreateRewardRequest request, long userId) {
        CreateRewardCommand.Builder commandBuilder = CreateRewardCommand.builder(
            request.getName(),
            request.getDescription(),
            request.getTermsCondition(),
            request.getCost(),
            userId
        ).stock(request.getStock());

        boolean startPeriodIsProvided = request.getStartPeriod() != null && !request.getStartPeriod().equals("");
        boolean endPeriodIsProvided = request.getEndPeriod() != null && !request.getEndPeriod().equals("");

        if (startPeriodIsProvided && endPeriodIsProvided) {
            try {
                Date startPeriod = parseStringDateToDate(request.getStartPeriod());
                Date endPeriod = parseStringDateToDate(request.getEndPeriod());
                commandBuilder.periods(startPeriod, endPeriod);
            } catch (ParseException e) {
                throw new IllegalArgumentException("Invalid Date Format Provided (valid: yyyy-mm-dd; e.g. 2022-12-20)");
            }
        } else if (startPeriodIsProvided || endPeriodIsProvided) {
            throw new IllegalArgumentException("Both Start Period & End Period must be provided or must be blank");
        }

        return commandBuilder.build();
    }

    private Date parseStringDateToDate(String stringDate) throws ParseException {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-M-dd");
        return formatter.parse(stringDate);
    }

    private ListMembershipRewardsQuery buildListMembershipRewardsQuery(Integer maxCost, Integer minCost, String query, long userId) {
        return ListMembershipRewardsQuery.builder(userId)
            .maxCost(maxCost)
            .minCost(minCost)
            .query(query)
            .build();
    }

    private Long getUserIdFromAuthentication(Authentication authentication) {
        Jwt principal = (Jwt) authentication.getPrincipal();
        return jwtTokenService.getUserId(principal);
    }
}
