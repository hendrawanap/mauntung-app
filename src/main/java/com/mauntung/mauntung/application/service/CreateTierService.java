package com.mauntung.mauntung.application.service;

import com.mauntung.mauntung.application.exception.RewardNotFoundException;
import com.mauntung.mauntung.application.port.reward.RewardRepository;
import com.mauntung.mauntung.application.port.tier.CreateTierCommand;
import com.mauntung.mauntung.application.port.tier.CreateTierResponse;
import com.mauntung.mauntung.application.port.tier.CreateTierUseCase;
import com.mauntung.mauntung.application.port.tier.TierRepository;
import com.mauntung.mauntung.domain.model.membership.Tier;
import com.mauntung.mauntung.domain.model.membership.TierFactory;
import com.mauntung.mauntung.domain.model.membership.TierFactoryImpl;
import com.mauntung.mauntung.domain.model.reward.Reward;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
@RequiredArgsConstructor
public class CreateTierService implements CreateTierUseCase {
    private final TierFactory tierFactory = new TierFactoryImpl();
    private final RewardRepository rewardRepository;
    private final TierRepository tierRepository;

    @Override
    public CreateTierResponse apply(CreateTierCommand command) {
        Set<Reward> rewards = findAllRewardsByIds(command.getRewardIds());
        Tier tier = buildTier(command, rewards);
        Long tierId = saveTierAndGetId(tier);

        return buildResponse(tier, tierId);
    }

    private Set<Reward> findAllRewardsByIds(Set<Long> ids) throws RewardNotFoundException {
        Set<Reward> rewards = rewardRepository.findAllById(ids);
        if (rewards.size() != ids.size())
            throw new RewardNotFoundException();
        return rewards;
    }

    private Long saveTierAndGetId(Tier tier) {
        return tierRepository.save(tier).orElseThrow(() -> new RuntimeException("Can't Create Tier"));
    }

    private CreateTierResponse buildResponse(Tier tier, long tierId) {
        return new CreateTierResponse(
            tierId,
            tier.getName(),
            tier.getRewards().size(),
            tier.getRequiredPoints(),
            tier.getMultiplierFactor()
        );
    }

    private Tier buildTier(CreateTierCommand command, Set<Reward> rewards) throws IllegalArgumentException {
        return tierFactory.builder(command.getName(), rewards, command.getRequiredPoints())
            .multiplierFactor(command.getMultiplierFactor())
            .build();
    }
}
