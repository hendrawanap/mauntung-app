package com.mauntung.mauntung.application.service;

import com.mauntung.mauntung.application.exception.RewardNotFoundException;
import com.mauntung.mauntung.application.port.reward.RewardRepository;
import com.mauntung.mauntung.application.port.tier.CreateTierCommand;
import com.mauntung.mauntung.application.port.tier.TierRepository;
import com.mauntung.mauntung.domain.model.reward.Reward;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CreateTierServiceTest {
    private static RewardRepository rewardRepository;
    private static TierRepository tierRepository;

    @BeforeAll
    static void beforeAll() {
        rewardRepository = mock(RewardRepository.class);
        tierRepository = mock(TierRepository.class);
    }
    @Test
    void givenNonExistingRewardId_apply_shouldThrowsException() {
        Set<Long> rewardIds = Set.of(1L, 2L, 3L);
        Set<Reward> rewards = Set.of(
            mock(Reward.class),
            mock(Reward.class)
        );

        when(rewardRepository.findAllById(rewardIds)).thenReturn(rewards);

        CreateTierCommand command = new CreateTierCommand("name", rewardIds, 10, 1.0F);
        CreateTierService service = new CreateTierService(rewardRepository, tierRepository);

        assertThrows(RewardNotFoundException.class, () -> service.apply(command));
    }

    @Test
    void givenTierRepositorySaveReturnEmpty_apply_shouldThrowsException() {
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

        assertThrows(RuntimeException.class, () -> service.apply(command));
    }

    @Test
    void givenTierRepositorySaveReturnLong_apply_shouldReturnResponse() {
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

        assertNotNull(service.apply(command));
    }
}