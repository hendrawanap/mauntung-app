package com.mauntung.mauntung.domain.model.membership;

import com.fasterxml.jackson.annotation.JsonValue;
import com.mauntung.mauntung.domain.common.MessageBuilder;
import lombok.Getter;

import java.util.Set;

@Getter
public class PointRules {
    private final int redeemTtl;
    private final int pointClaimableDuration;
    private final int pointUsableDuration;
    private final DistributionMethod distributionMethod;
    private final Set<RewardClaimMethod> rewardClaimMethods;
    private final PointGeneration pointGeneration;

    public PointRules(int redeemTtl, int pointClaimableDuration, int pointUsableDuration, DistributionMethod distributionMethod, Set<RewardClaimMethod> rewardClaimMethods, PointGeneration pointGeneration) throws IllegalArgumentException {
        validate(redeemTtl, pointClaimableDuration, pointUsableDuration, rewardClaimMethods);

        this.redeemTtl = redeemTtl;
        this.pointClaimableDuration = pointClaimableDuration;
        this.pointUsableDuration = pointUsableDuration;
        this.distributionMethod = distributionMethod;
        this.rewardClaimMethods = rewardClaimMethods;
        this.pointGeneration = pointGeneration;
    }

    private static void validate(int redeemTtl, int pointClaimableDuration, int pointUsableDuration, Set<RewardClaimMethod> rewardClaimMethods) throws IllegalArgumentException {
        MessageBuilder mb = new MessageBuilder();

        if (redeemTtl < 1) mb.append("Redeem TTL must be larger than 0");

        if (pointClaimableDuration < 1) mb.append("Point Claimable Duration must be larger than 0");

        if (pointUsableDuration < 1) mb.append("Point Usable Duration must be larger than 0");

        if (rewardClaimMethods.isEmpty()) mb.append("Reward Claim Methods must not be empty");

        if (!mb.isEmpty()) throw new IllegalArgumentException(mb.toString());
    }

    public enum DistributionMethod {
        POINT_CODE_GENERATION("generate"),
        CUSTOMER_CODE_SCANNING("scan");

        public final String label;

        DistributionMethod(String label) {
            this.label = label.toUpperCase();
        }

        public static DistributionMethod fromStringLabel(String label) {
            for (DistributionMethod constant : DistributionMethod.values()) {
                if (constant.label.equalsIgnoreCase(label))
                    return constant;
            }
            throw new IllegalArgumentException(String.format("No enum constant %s.%s", DistributionMethod.class, label));
        }

        @JsonValue
        @Override
        public String toString() {
            return label;
        }
    }

    public enum RewardClaimMethod {
        BY_CUSTOMER("customer"),
        BY_MERCHANT("merchant");

        public final String label;

        RewardClaimMethod(String label) {
            this.label = label.toUpperCase();
        }

        public static RewardClaimMethod fromStringLabel(String label) {
            for (RewardClaimMethod constant : RewardClaimMethod.values()) {
                if (constant.label.equalsIgnoreCase(label))
                    return constant;
            }
            throw new IllegalArgumentException(String.format("No enum constant %s.%s", RewardClaimMethod.class, label));
        }

        @JsonValue
        @Override
        public String toString() {
            return label;
        }
    }
}
