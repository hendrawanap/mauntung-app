package com.mauntung.mauntung.adapter.persistence.membership;

import com.mauntung.mauntung.adapter.persistence.merchant.MerchantEntity;
import com.mauntung.mauntung.adapter.persistence.merchant.MerchantMapper;
import com.mauntung.mauntung.adapter.persistence.reward.RewardEntity;
import com.mauntung.mauntung.adapter.persistence.reward.RewardMapper;
import com.mauntung.mauntung.adapter.persistence.tier.TierEntity;
import com.mauntung.mauntung.adapter.persistence.tier.TierMapper;
import com.mauntung.mauntung.adapter.persistence.user.UserEntity;
import com.mauntung.mauntung.application.port.membership.MembershipRepository;
import com.mauntung.mauntung.domain.model.membership.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.util.Date;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class MembershipRepositoryImplTest {
    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private JpaMembershipRepository jpaMembershipRepository;

    @Test
    void givenPointMembershipWithTiers_save_shouldReturnId() {
        RewardMapper rewardMapper = new RewardMapper();
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

        TierMapper tierMapper = new TierMapper();
        Set<TierEntity> tierEntities = new HashSet<>();
        TierEntity tierEntity1 = TierEntity.builder()
            .name("bronze")
            .requiredPoints(0)
            .multiplierFactor(1F)
            .rewards(rewardEntities)
            .build();
        TierEntity tierEntity2 = TierEntity.builder()
            .name("silver")
            .requiredPoints(100)
            .multiplierFactor(1F)
            .rewards(rewardEntities)
            .build();
        TierEntity tierEntity3 = TierEntity.builder()
            .name("gold")
            .requiredPoints(200)
            .multiplierFactor(1F)
            .rewards(rewardEntities)
            .build();
        tierEntities.add(entityManager.persist(tierEntity1));
        tierEntities.add(entityManager.persist(tierEntity2));
        tierEntities.add(entityManager.persist(tierEntity3));

        UserEntity userEntity = UserEntity.builder()
            .role("merchant")
            .email("merchant@mail.com")
            .password("password")
            .build();
        userEntity = entityManager.persist(userEntity);

        MerchantMapper merchantMapper = new MerchantMapper();
        MerchantEntity merchantEntity = MerchantEntity.builder()
            .user(userEntity)
            .name("name")
            .build();
        merchantEntity = entityManager.persist(merchantEntity);

        entityManager.flush();

        PointMembershipFactory pointMembershipFactory = new PointMembershipFactoryImpl();
        PointRules rules = new PointRules(
            10,
            10,
            10,
            PointRules.DistributionMethod.POINT_CODE_GENERATION,
            Set.of(PointRules.RewardClaimMethod.BY_CUSTOMER),
            new PointGeneration(PointGeneration.TYPE_NOMINAL, 10, 10_000)
        );
        Membership membership = pointMembershipFactory.builder(
            "name",
            merchantMapper.entityToModel(merchantEntity),
            rewardEntities.stream().map(rewardMapper::entityToModel).collect(Collectors.toSet()),
            new Date(),
            rules
        )
            .tiers(tierEntities.stream().map(tierMapper::entityToModel).collect(Collectors.toSet()))
            .build();
        MembershipRepository membershipRepository = new MembershipRepositoryImpl(jpaMembershipRepository);
        Optional<Long> membershipId = membershipRepository.save(membership);
        assertTrue(membershipId.isPresent());
    }

    @Test
    void givenStampMembership_save_shouldReturnId() {
        RewardMapper rewardMapper = new RewardMapper();
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

        UserEntity userEntity = UserEntity.builder()
            .role("merchant")
            .email("merchant@mail.com")
            .password("password")
            .build();
        userEntity = entityManager.persist(userEntity);

        MerchantMapper merchantMapper = new MerchantMapper();
        MerchantEntity merchantEntity = MerchantEntity.builder()
            .user(userEntity)
            .name("name")
            .build();
        merchantEntity = entityManager.persist(merchantEntity);

        entityManager.flush();

        StampMembershipFactory stampMembershipFactory = new StampMembershipFactoryImpl();
        StampRules rules = new StampRules(10, 10, 10);
        Membership membership = stampMembershipFactory.builder(
            "name",
            merchantMapper.entityToModel(merchantEntity),
            rewardEntities.stream().map(rewardMapper::entityToModel).collect(Collectors.toSet()),
            new Date(),
            rules
        ).build();
        MembershipRepository membershipRepository = new MembershipRepositoryImpl(jpaMembershipRepository);
        Optional<Long> membershipId = membershipRepository.save(membership);
        assertTrue(membershipId.isPresent());
    }
}