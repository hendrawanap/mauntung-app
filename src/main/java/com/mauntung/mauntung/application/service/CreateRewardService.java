package com.mauntung.mauntung.application.service;

import com.mauntung.mauntung.application.port.reward.CreateRewardCommand;
import com.mauntung.mauntung.application.port.reward.CreateRewardResponse;
import com.mauntung.mauntung.application.port.reward.CreateRewardUseCase;
import com.mauntung.mauntung.application.port.reward.RewardRepository;
import com.mauntung.mauntung.domain.model.reward.Reward;
import com.mauntung.mauntung.domain.model.reward.RewardFactory;
import com.mauntung.mauntung.domain.model.reward.RewardFactoryImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class CreateRewardService implements CreateRewardUseCase {
    private final RewardRepository rewardRepository;
    private final RewardFactory rewardFactory = new RewardFactoryImpl();

    @Override
    public CreateRewardResponse apply(CreateRewardCommand command) {
        CreateRewardResponse response = new CreateRewardResponse();

        Reward newReward;
        try {
            newReward = buildReward(command);
        } catch (IllegalArgumentException ex) {
            response.setErrorResponse(ex.getMessage());
            return response;
        }

        Optional<Long> rewardId = rewardRepository.save(newReward);
        if (rewardId.isEmpty()) {
            response.setErrorResponse("Can't create reward");
            return response;
        }

        response.setSuccessResponse(new CreateRewardResponse.SuccessResponse(rewardId.get(), new Date()));
        return response;
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
