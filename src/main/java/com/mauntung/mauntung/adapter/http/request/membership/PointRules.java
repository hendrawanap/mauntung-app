package com.mauntung.mauntung.adapter.http.request.membership;

import com.mauntung.mauntung.common.validator.ValueOfEnum;
import lombok.Data;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.util.Set;

@Data
public class PointRules {
    @NotNull
    @Positive
    private Integer redeemTtl;

    @NotNull
    @Positive
    private Integer pointClaimableDuration;

    @NotNull
    @Positive
    private Integer pointUsableDuration;

    @NotNull
    @ValueOfEnum(
        enumClass = com.mauntung.mauntung.domain.model.membership.PointRules.DistributionMethod.class,
        message = "with value '${validatedValue}' is not valid enum"
    )
    private String distributionMethod;

    @NotNull
    private Set<@ValueOfEnum(enumClass = com.mauntung.mauntung.domain.model.membership.PointRules.RewardClaimMethod.class, message = "with value '${validatedValue}' is not valid enum") String> rewardClaimMethods;

    @Valid
    @NotNull
    private PointGeneration pointGeneration;

    @Data
    public static class PointGeneration {
        @NotNull
        @ValueOfEnum(
            enumClass = com.mauntung.mauntung.domain.model.membership.PointGeneration.Type.class,
            message = "with value '${validatedValue}' is not valid enum"
        )
        private String type;

        @NotNull
        @Positive
        private Integer points;

        @Positive
        private Integer divider;
    }
}
