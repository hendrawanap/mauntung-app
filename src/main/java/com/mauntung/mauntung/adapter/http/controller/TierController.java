package com.mauntung.mauntung.adapter.http.controller;

import com.mauntung.mauntung.adapter.http.request.tier.CreateTierRequest;
import com.mauntung.mauntung.adapter.http.response.tier.CreateTierResponseBody;
import com.mauntung.mauntung.application.port.tier.CreateTierCommand;
import com.mauntung.mauntung.application.port.tier.CreateTierResponse;
import com.mauntung.mauntung.application.port.tier.CreateTierUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
public class TierController {
    private final CreateTierUseCase createTierUseCase;

    @PreAuthorize("hasRole('MERCHANT')")
    @PostMapping("/merchant/tiers")
    public ResponseEntity<CreateTierResponseBody> createTier(@RequestBody @Valid CreateTierRequest request) {
        CreateTierCommand command = buildCreateTierCommand(request);
        CreateTierResponse response = createTierUseCase.apply(command);
        return new ResponseEntity<>(new CreateTierResponseBody(response), HttpStatus.CREATED);
    }

    private CreateTierCommand buildCreateTierCommand(CreateTierRequest request) {
        return new CreateTierCommand(
            request.getName(),
            request.getRewardIds(),
            request.getRequiredPoints(),
            request.getMultiplierFactor()
        );
    }
}
