package com.mauntung.mauntung.adapter.http.controller;

import com.mauntung.mauntung.adapter.http.request.membership.CreateMembershipRequest;
import com.mauntung.mauntung.adapter.http.request.membership.mapper.CreateMembershipRequestMapper;
import com.mauntung.mauntung.adapter.http.response.membership.CreateMembershipResponseBody;
import com.mauntung.mauntung.adapter.http.security.JwtTokenService;
import com.mauntung.mauntung.application.port.membership.*;
import com.mauntung.mauntung.domain.model.membership.Membership;
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
    private final CreateMembershipRequestMapper createMembershipRequestMapper = new CreateMembershipRequestMapper();

    @PreAuthorize("hasRole('MERCHANT')")
    @PostMapping("merchant/membership")
    public ResponseEntity<CreateMembershipResponseBody> createMembership(@RequestBody @Valid CreateMembershipRequest request, Authentication authentication) {
        Long userId = getUserIdFromAuthentication(authentication);
        boolean isPointMembership = request.getType().equalsIgnoreCase(Membership.Type.POINT.toString());
        boolean isStampMembership = request.getType().equalsIgnoreCase(Membership.Type.STAMP.toString());

        if (isPointMembership) {
            CreatePointMembershipCommand command = buildCreatePointMembershipCommand(request, userId);
            CreatePointMembershipResponse response = createPointMembershipUseCase.apply(command);
            return new ResponseEntity<>(new CreateMembershipResponseBody(response), HttpStatus.CREATED);
        } else if (isStampMembership) {
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
        if (request.getRules().getPoint() == null)
            throw new IllegalArgumentException("rules.point must not be null");

        return createMembershipRequestMapper.mapToCreatePointMembershipCommand(request, userId);
    }

    private CreateStampMembershipCommand buildCreateStampMembershipCommand(CreateMembershipRequest request, Long userId) {
        if (request.getRules().getStamp() == null)
            throw new IllegalArgumentException("rules.stamp must not be null");

        return createMembershipRequestMapper.mapToCreateStampMembershipCommand(request, userId);
    }
}
