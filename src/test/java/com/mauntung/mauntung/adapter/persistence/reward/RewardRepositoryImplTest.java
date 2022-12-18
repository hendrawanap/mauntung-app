package com.mauntung.mauntung.adapter.persistence.reward;

import com.mauntung.mauntung.application.port.reward.RewardRepository;
import com.mauntung.mauntung.domain.model.reward.Reward;
import com.mauntung.mauntung.domain.model.reward.RewardFactory;
import com.mauntung.mauntung.domain.model.reward.RewardFactoryImpl;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class RewardRepositoryImplTest {
    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private JpaRewardRepository jpaRewardRepository;

    @Test
    void save_shouldReturnId() {
        RewardFactory rewardFactory = new RewardFactoryImpl();
        Reward reward = rewardFactory.builder("name", "desc", "terms", 10).build();
        RewardRepository rewardRepository = new RewardRepositoryImpl(jpaRewardRepository);
        Optional<Long> rewardId = rewardRepository.save(reward);
        assertTrue(rewardId.isPresent());
    }

    @Test
    void findAllById_shouldReturnRewards() {
        Set<Long> rewardIds = new HashSet<>();
        RewardEntity rewardEntity = RewardEntity.builder()
            .name("name")
            .description("desc")
            .termsCondition("terms")
            .cost(10)
            .build();
        for (int i = 0; i < 3; i++) {
            rewardIds.add(entityManager.persist(rewardEntity).getId());
        }
        entityManager.flush();

        RewardRepository rewardRepository = new RewardRepositoryImpl(jpaRewardRepository);
        Set<Reward> rewards = rewardRepository.findAllById(rewardIds);
        assertEquals(rewardIds.size(), rewards.size());
    }
}