package com.mauntung.mauntung.adapter.persistence.tier;

import com.mauntung.mauntung.adapter.persistence.reward.RewardEntity;
import com.mauntung.mauntung.adapter.persistence.reward.RewardMapper;
import com.mauntung.mauntung.application.port.tier.TierRepository;
import com.mauntung.mauntung.domain.model.membership.Tier;
import com.mauntung.mauntung.domain.model.reward.Reward;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class TierRepositoryImplTest {
    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private JpaTierRepository jpaTierRepository;

    @Test
    void save_shouldReturnId() {
        RewardMapper rewardMapper = new RewardMapper();
        Set<Reward> rewards = new HashSet<>();
        RewardEntity rewardEntity = RewardEntity.builder()
            .name("name")
            .description("desc")
            .termsCondition("terms")
            .cost(10)
            .build();
        for (int i = 0; i < 3; i++) {
            rewards.add(rewardMapper.entityToModel(entityManager.persist(rewardEntity)));
        }
        entityManager.flush();

        Tier tier = Tier.withoutId("name", rewards, 10, 1F);
        TierRepository tierRepository = new TierRepositoryImpl(jpaTierRepository);
        Optional<Long> tierId = tierRepository.save(tier);
        assertTrue(tierId.isPresent());
    }

    @Test
    void findAllById_shouldReturnTiers() {
        Set<RewardEntity> rewardEntities = new HashSet<>();
        RewardEntity rewardEntity = RewardEntity.builder()
            .name("name")
            .description("desc")
            .termsCondition("terms")
            .cost(10)
            .build();
        for (int i = 0; i < 3; i++) {
            rewardEntities.add(entityManager.persist(rewardEntity));
        }
        Set<Long> tierIds = new HashSet<>();
        TierEntity tierEntity = TierEntity.builder()
            .name("name")
            .multiplierFactor(1F)
            .requiredPoints(10)
            .rewards(rewardEntities)
            .build();
        for (int i = 0; i < 3; i++) {
            tierIds.add(entityManager.persist(tierEntity).getId());
        }
        entityManager.flush();

        TierRepository tierRepository = new TierRepositoryImpl(jpaTierRepository);
        Set<Tier> tiers = tierRepository.findAllById(tierIds);
        assertEquals(tierIds.size(), tiers.size());
    }
}