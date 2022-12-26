package com.mauntung.mauntung.adapter.http.request.membership.mapper;

import com.mauntung.mauntung.adapter.http.request.membership.CreateMembershipRequest;
import com.mauntung.mauntung.adapter.http.request.membership.StampRules;
import com.mauntung.mauntung.application.port.membership.CreatePointMembershipCommand;
import com.mauntung.mauntung.application.port.membership.CreateStampMembershipCommand;
import com.mauntung.mauntung.domain.model.membership.PointGeneration;
import com.mauntung.mauntung.domain.model.membership.PointRules;

import java.util.Set;
import java.util.stream.Collectors;

public class CreateMembershipRequestMapper {
    public CreatePointMembershipCommand mapToCreatePointMembershipCommand(CreateMembershipRequest request, Long userId) {
        PointRules domainPointRules = mapRequestPointRulesToDomainPointRules(request.getRules().getPoint());
        return new CreatePointMembershipCommand(
            request.getName(),
            userId,
            request.getRewardIds(),
            domainPointRules,
            request.getTierIds()
        );
    }

    public CreateStampMembershipCommand mapToCreateStampMembershipCommand(CreateMembershipRequest request, Long userId) {
        StampRules requestPointRules = request.getRules().getStamp();
        return new CreateStampMembershipCommand(
            request.getName(),
            userId,
            request.getRewardIds(),
            requestPointRules.getRedeemTtl(),
            requestPointRules.getUsableDuration(),
            requestPointRules.getCardCapacity()
        );
    }

    private PointRules mapRequestPointRulesToDomainPointRules(com.mauntung.mauntung.adapter.http.request.membership.PointRules request) {
        PointRules.DistributionMethod distributionMethod = PointRules.DistributionMethod.fromStringLabel(request.getDistributionMethod());

        Set<PointRules.RewardClaimMethod> rewardClaimMethods = request.getRewardClaimMethods()
            .stream()
            .map(PointRules.RewardClaimMethod::fromStringLabel)
            .collect(Collectors.toSet());

        PointGeneration pointGeneration = new PointGeneration(
            PointGeneration.Type.valueOf(request.getPointGeneration().getType().toUpperCase()),
            request.getPointGeneration().getPoints(),
            request.getPointGeneration().getDivider()
        );

        return new PointRules(
            request.getRedeemTtl(),
            request.getPointClaimableDuration(),
            request.getPointUsableDuration(),
            distributionMethod,
            rewardClaimMethods,
            pointGeneration
        );
    }
}
