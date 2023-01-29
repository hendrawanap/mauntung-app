package com.mauntung.mauntung.adapter.persistence.membership;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mauntung.mauntung.adapter.persistence.merchant.MerchantEntity;
import com.mauntung.mauntung.adapter.persistence.user.UserEntity;
import com.mauntung.mauntung.domain.model.membership.*;
import com.mauntung.mauntung.domain.model.user.User;
import org.junit.jupiter.api.Test;

import java.util.Date;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class MembershipMapperTest {
    private final ObjectMapper jsonMapper = new ObjectMapper();
    private final MembershipMapper membershipMapper = new MembershipMapper();

    private String createJsonPointRules() throws JsonProcessingException {
        PointRules pointRules = createDummyPointRules();
        return jsonMapper.writeValueAsString(pointRules);
    }

    private PointRules createDummyPointRules() {
        return new PointRules(
            10,
            10,
            10,
            PointRules.DistributionMethod.POINT_CODE_GENERATION,
            Set.of(PointRules.RewardClaimMethod.BY_CUSTOMER),
            new PointGeneration(
                PointGeneration.Type.NOMINAL,
                10,
                10_000
            )
        );
    }

    @Test
    void entityToModel_shouldNotThrowsException() throws JsonProcessingException {
        String jsonPointRules = createJsonPointRules();

        MembershipEntity entity = MembershipEntity.builder()
            .type(Membership.Type.POINT.toString())
            .merchant(MerchantEntity.builder()
                .name("merchant name")
                .phone("+6281938212341")
                .user(UserEntity.builder()
                    .role(User.Role.MERCHANT.toString())
                    .email("user@mail.com")
                    .password("password123")
                    .build())
                .build())
            .rules(jsonPointRules)
            .rewards(Set.of())
            .isFinalized(false)
            .build();

        assertDoesNotThrow(() -> membershipMapper.entityToModel(entity));

        Membership membership = membershipMapper.entityToModel(entity);
        assertTrue(membership instanceof PointMembership);

        PointRules pointRules = ((PointMembership) membership).getRules();
        assertEquals(10, pointRules.getRedeemTtl());
        assertEquals(PointRules.DistributionMethod.POINT_CODE_GENERATION, pointRules.getDistributionMethod());
        assertEquals(10, pointRules.getPointGeneration().getPoints());
        assertEquals(PointGeneration.Type.NOMINAL, pointRules.getPointGeneration().getType());
    }

    @Test
    void modelToEntity_shouldNotThrowsException() throws JsonProcessingException {
        Membership membership = new PointMembershipFactoryImpl().builder(
            "name",
            Set.of(),
            new Date(),
            createDummyPointRules(),
            false
        ).build();

        assertDoesNotThrow(() -> membershipMapper.modelToEntity(membership));

        MembershipEntity entity = membershipMapper.modelToEntity(membership);
        String jsonPointRules = entity.getRules();
        assertEquals(createJsonPointRules(), jsonPointRules);
    }
}