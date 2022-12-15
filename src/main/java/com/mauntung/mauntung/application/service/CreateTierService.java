package com.mauntung.mauntung.application.service;

import com.mauntung.mauntung.application.port.reward.RewardRepository;
import com.mauntung.mauntung.application.port.tier.CreateTierCommand;
import com.mauntung.mauntung.application.port.tier.CreateTierResponse;
import com.mauntung.mauntung.application.port.tier.CreateTierUseCase;
import com.mauntung.mauntung.application.port.tier.TierRepository;
import com.mauntung.mauntung.domain.model.membership.Tier;
import com.mauntung.mauntung.domain.model.reward.Reward;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.Set;

@Component
@RequiredArgsConstructor
public class CreateTierService implements CreateTierUseCase {
    private final RewardRepository rewardRepository;
    private final TierRepository tierRepository;

    @Override
    public CreateTierResponse apply(CreateTierCommand command) {
        CreateTierResponse response = new CreateTierResponse();

        Set<Reward> rewards = rewardRepository.findAllById(command.getRewardIds());
        if (rewards.size() != command.getRewardIds().size()) {
            response.setErrorResponse("Reward not found");
            return response;
        }

        Tier tier;
        try {
            tier = createTier(command, rewards);
        } catch (IllegalArgumentException ex) {
            response.setErrorResponse(ex.getMessage());
            return response;
        }

        Optional<Long> tierId = tierRepository.save(tier);
        if (tierId.isEmpty()) {
            response.setErrorResponse("Can't create tier");
            return response;
        }

        response.setSuccessResponse(new CreateTierResponse.SuccessResponse(tier, tierId.get()));
        return response;
    }

    private Tier createTier(CreateTierCommand command, Set<Reward> rewards) throws IllegalArgumentException {
        return Tier.withoutId(command.getName(), rewards, command.getRequiredPoints(), command.getMultiplierFactor());
    }
}
