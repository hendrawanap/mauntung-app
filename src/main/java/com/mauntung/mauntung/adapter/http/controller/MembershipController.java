package com.mauntung.mauntung.adapter.http.controller;

import com.mauntung.mauntung.adapter.http.request.membership.CreateMembershipRequest;
import com.mauntung.mauntung.adapter.http.response.membership.CreateMembershipResponseBody;
import com.mauntung.mauntung.adapter.http.security.JwtTokenService;
import com.mauntung.mauntung.application.port.membership.*;
import com.mauntung.mauntung.domain.model.membership.StampRules;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
public class MembershipController {
    private final CreatePointMembershipUseCase createPointMembershipUseCase;
    private final CreateStampMembershipUseCase createStampMembershipUseCase;
    private final JwtTokenService jwtTokenService;

    @PreAuthorize("hasRole('MERCHANT')")
    @PostMapping("merchant/membership")
    public ResponseEntity<CreateMembershipResponseBody> createMembership(@RequestBody @Valid CreateMembershipRequest request, Authentication authentication) {
        Long userId = getUserIdFromAuthentication(authentication);

        if (request.getType().equalsIgnoreCase("point")) {
            CreatePointMembershipCommand command = buildCreatePointMembershipCommand(request, userId);
            CreatePointMembershipResponse response = createPointMembershipUseCase.apply(command);
            return new ResponseEntity<>(new CreateMembershipResponseBody(response), HttpStatus.CREATED);
        } else if (request.getType().equalsIgnoreCase("stamp")) {
            CreateStampMembershipCommand command = buildCreateStampMembershipCommand(request, userId);
            CreateStampMembershipResponse response = createStampMembershipUseCase.apply(command);
            return new ResponseEntity<>(new CreateMembershipResponseBody(response), HttpStatus.CREATED);
        }

        throw new IllegalArgumentException("Invalid type provided");
    }

    private Long getUserIdFromAuthentication(Authentication authentication) {
        Jwt principal = (Jwt) authentication.getPrincipal();
        return jwtTokenService.getUserId(principal);
    }

    private CreatePointMembershipCommand buildCreatePointMembershipCommand(CreateMembershipRequest request, Long userId) {
        return new CreatePointMembershipCommand(
            request.getName(),
            userId,
            request.getRewardIds(),
            request.getRules().getPoint(),
            request.getTierIds()
        );
    }

    private CreateStampMembershipCommand buildCreateStampMembershipCommand(CreateMembershipRequest request, Long userId) {
        StampRules rules = request.getRules().getStamp();
        return new CreateStampMembershipCommand(
            request.getName(),
            userId,
            request.getRewardIds(),
            rules.getRedeemTtl(),
            rules.getUsableDuration(),
            rules.getCardCapacity()
        );
    }
}
