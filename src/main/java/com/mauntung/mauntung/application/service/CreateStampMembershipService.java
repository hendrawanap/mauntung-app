package com.mauntung.mauntung.application.service;

import com.mauntung.mauntung.application.port.membership.CreateStampMembershipCommand;
import com.mauntung.mauntung.application.port.membership.CreateStampMembershipResponse;
import com.mauntung.mauntung.application.port.membership.CreateStampMembershipUseCase;
import com.mauntung.mauntung.application.port.membership.MembershipRepository;
import com.mauntung.mauntung.application.port.merchant.MerchantRepository;
import com.mauntung.mauntung.application.port.reward.RewardRepository;
import com.mauntung.mauntung.domain.model.membership.StampMembership;
import com.mauntung.mauntung.domain.model.membership.StampMembershipFactory;
import com.mauntung.mauntung.domain.model.membership.StampMembershipFactoryImpl;
import com.mauntung.mauntung.domain.model.membership.StampRules;
import com.mauntung.mauntung.domain.model.merchant.Merchant;
import com.mauntung.mauntung.domain.model.reward.Reward;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.Optional;
import java.util.Set;

@Component
@RequiredArgsConstructor
public class CreateStampMembershipService implements CreateStampMembershipUseCase {
    private final MerchantRepository merchantRepository;
    private final RewardRepository rewardRepository;
    private final MembershipRepository membershipRepository;
    private final StampMembershipFactory membershipFactory = new StampMembershipFactoryImpl();

    @Override
    public CreateStampMembershipResponse apply(CreateStampMembershipCommand command) {
        CreateStampMembershipResponse response = new CreateStampMembershipResponse();

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

        StampMembership membership;
        try {
            membership = buildStampMembership(command, merchant.get(), rewards);
        } catch (IllegalArgumentException ex) {
            response.setErrorResponse(ex.getMessage());
            return response;
        }

        Optional<Long> membershipId = membershipRepository.save(membership);
        if (membershipId.isEmpty()) {
            response.setErrorResponse("Can't create stamp membership");
            return response;
        }

        rewardRepository.attachToMembership(rewards, membershipId.get());

        response.setSuccessResponse(new CreateStampMembershipResponse.SuccessResponse(
            membershipId.get(),
            membership.getName(),
            membership.getRewardsQty(),
            membership.getRules().getCardCapacity(),
            membership.getCreatedAt()
        ));
        return response;
    }

    private StampMembership buildStampMembership(CreateStampMembershipCommand command, Merchant merchant, Set<Reward> rewards) throws IllegalArgumentException {
        return membershipFactory.builder(
            command.getName(),
            merchant,
            rewards,
            new Date(),
            new StampRules(command.getRedeemTtl(), command.getUsableDuration(), command.getCardCapacity())
        ).build();
    }
}
