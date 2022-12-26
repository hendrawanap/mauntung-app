package com.mauntung.mauntung.application.service;

import com.mauntung.mauntung.application.port.reward.CreateRewardCommand;
import com.mauntung.mauntung.application.port.reward.CreateRewardResponse;
import com.mauntung.mauntung.application.port.reward.CreateRewardUseCase;
import com.mauntung.mauntung.application.port.reward.RewardRepository;
import com.mauntung.mauntung.domain.model.reward.Reward;
import com.mauntung.mauntung.domain.model.reward.RewardFactory;
import com.mauntung.mauntung.domain.model.reward.RewardFactoryImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
@RequiredArgsConstructor
public class CreateRewardService implements CreateRewardUseCase {
    private final RewardRepository rewardRepository;
    private final RewardFactory rewardFactory = new RewardFactoryImpl();

    @Override
    public CreateRewardResponse apply(CreateRewardCommand command) {
        Reward reward = buildReward(command);
        Long rewardId = saveRewardAndGetId(reward);

        return buildResponse(rewardId);
    }

    private Long saveRewardAndGetId(Reward reward) throws IllegalArgumentException {
        return rewardRepository.save(reward).orElseThrow(() -> new RuntimeException("Can't Create Reward"));
    }

    private CreateRewardResponse buildResponse(long rewardId) {
        return new CreateRewardResponse(rewardId, new Date());
    }

    private Reward buildReward(CreateRewardCommand command) throws IllegalArgumentException {
        return rewardFactory.builder(
                command.getName(),
                command.getDescription(),
                command.getTermsCondition(),
                command.getCost()
            )
            .startPeriod(command.getStartPeriod())
            .endPeriod(command.getEndPeriod())
            .stock(command.getStock())
            .build();
    }
}
