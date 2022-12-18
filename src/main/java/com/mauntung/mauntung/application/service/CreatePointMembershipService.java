package com.mauntung.mauntung.application.service;

import com.mauntung.mauntung.application.port.membership.*;
import com.mauntung.mauntung.application.port.merchant.MerchantRepository;
import com.mauntung.mauntung.application.port.reward.RewardRepository;
import com.mauntung.mauntung.application.port.tier.TierRepository;
import com.mauntung.mauntung.domain.model.membership.*;
import com.mauntung.mauntung.domain.model.merchant.Merchant;
import com.mauntung.mauntung.domain.model.reward.Reward;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.Optional;
import java.util.Set;

@Component
@RequiredArgsConstructor
public class CreatePointMembershipService implements CreatePointMembershipUseCase {
    private final MerchantRepository merchantRepository;
    private final RewardRepository rewardRepository;
    private final TierRepository tierRepository;
    private final MembershipRepository membershipRepository;
    private final PointMembershipFactory membershipFactory = new PointMembershipFactoryImpl();

    @Override
    public CreatePointMembershipResponse apply(CreatePointMembershipCommand command) {
        CreatePointMembershipResponse response = new CreatePointMembershipResponse();

        Optional<Merchant> merchant = merchantRepository.findByUserId(command.getUserId());
        if (merchant.isEmpty()) {
            response.setErrorResponse("Merchant not found");
            return response;
        }

        Set<Reward> rewards = rewardRepository.findAllById(command.getRewardIds());
        if (rewards.size() != command.getRewardIds().size()) {
            response.setErrorResponse("Reward not found");
            return response;
        }

        Set<Tier> tiers = null;
        if (command.getTierIds() != null) {
            tiers = tierRepository.findAllById(command.getTierIds());

            if (tiers.size() != command.getTierIds().size()) {
                response.setErrorResponse("Tier not found");
                return response;
            }
        }

        PointMembership membership;
        try {
            membership = buildPointMembership(command, merchant.get(), rewards, tiers);
        } catch (IllegalArgumentException ex) {
            response.setErrorResponse(ex.getMessage());
            return response;
        }

        Optional<Long> membershipId = membershipRepository.save(membership);
        if (membershipId.isEmpty()) {
            response.setErrorResponse("Can't create point membership");
            return response;
        }

        rewardRepository.attachToMembership(rewards, membershipId.get());

        response.setSuccessResponse(new CreatePointMembershipResponse.SuccessResponse(
            membershipId.get(),
            membership.getName(),
            membership.getRewardsQty(),
            membership.getTiersQty(),
            membership.getCreatedAt()
        ));
        return response;
    }

    private PointMembership buildPointMembership(CreatePointMembershipCommand command, Merchant merchant, Set<Reward> rewards, Set<Tier> tiers) {
        return membershipFactory.builder(
            command.getName(),
            merchant,
            rewards,
            new Date(),
            command.getRules()
        )
            .tiers(tiers)
            .build();
    }
}
