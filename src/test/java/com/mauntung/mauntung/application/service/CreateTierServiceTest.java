package com.mauntung.mauntung.application.service;

import com.mauntung.mauntung.application.port.reward.RewardRepository;
import com.mauntung.mauntung.application.port.tier.CreateTierCommand;
import com.mauntung.mauntung.application.port.tier.CreateTierResponse;
import com.mauntung.mauntung.application.port.tier.TierRepository;
import com.mauntung.mauntung.domain.model.reward.Reward;
import org.junit.jupiter.api.Test;

import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CreateTierServiceTest {
    @Test
    void givenNonExistingRewardId_apply_shouldReturnErrorResponse() {
        RewardRepository rewardRepository = mock(RewardRepository.class);
        TierRepository tierRepository = mock(TierRepository.class);

        Set<Long> rewardIds = Set.of(1L, 2L, 3L);
        Set<Reward> rewards = Set.of(
            mock(Reward.class),
            mock(Reward.class)
        );

        when(rewardRepository.findAllById(rewardIds)).thenReturn(rewards);

        CreateTierCommand command = new CreateTierCommand("name", rewardIds, 10, 1.0F);
        CreateTierService service = new CreateTierService(rewardRepository, tierRepository);
        CreateTierResponse response = service.apply(command);

        assertNotNull(response.getErrorResponse());
        assertNull(response.getSuccessResponse());
    }

    @Test
    void givenTierRepositorySaveReturnEmpty_apply_shouldReturnErrorResponse() {
        RewardRepository rewardRepository = mock(RewardRepository.class);
        TierRepository tierRepository = mock(TierRepository.class);

        Set<Long> rewardIds = Set.of(1L, 2L, 3L);
        Set<Reward> rewards = Set.of(
            mock(Reward.class),
            mock(Reward.class),
            mock(Reward.class)
        );

        when(rewardRepository.findAllById(rewardIds)).thenReturn(rewards);
        when(tierRepository.save(any())).thenReturn(Optional.empty());

        CreateTierCommand command = new CreateTierCommand("name", rewardIds, 10, 1.0F);
        CreateTierService service = new CreateTierService(rewardRepository, tierRepository);
        CreateTierResponse response = service.apply(command);

        assertNotNull(response.getErrorResponse());
        assertNull(response.getSuccessResponse());
    }

    @Test
    void givenTierRepositorySaveReturnLong_apply_shouldReturnSuccessResponse() {
        RewardRepository rewardRepository = mock(RewardRepository.class);
        TierRepository tierRepository = mock(TierRepository.class);

        Set<Long> rewardIds = Set.of(1L, 2L, 3L);
        Set<Reward> rewards = Set.of(
            mock(Reward.class),
            mock(Reward.class),
            mock(Reward.class)
        );
        long tierId = 1;

        when(rewardRepository.findAllById(rewardIds)).thenReturn(rewards);
        when(tierRepository.save(any())).thenReturn(Optional.of(tierId));

        CreateTierCommand command = new CreateTierCommand("name", rewardIds, 10, 1.0F);
        CreateTierService service = new CreateTierService(rewardRepository, tierRepository);
        CreateTierResponse response = service.apply(command);

        assertNull(response.getErrorResponse());
        assertNotNull(response.getSuccessResponse());
    }
}