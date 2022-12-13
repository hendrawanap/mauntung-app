package com.mauntung.mauntung.domain.model.membership;

import com.mauntung.mauntung.domain.common.MessageBuilder;
import lombok.Getter;

import java.util.Set;
import java.util.stream.Collectors;

@Getter
public class PointRules {
    private final int redeemTtl;
    private final int pointClaimableDuration;
    private final int pointUsableDuration;
    private final String distributionMethod;
    private final Set<String> rewardClaimMethods;
    private final PointGeneration pointGeneration;

    public PointRules(int redeemTtl, int pointClaimableDuration, int pointUsableDuration, String distributionMethod, Set<String> rewardClaimMethods, PointGeneration pointGeneration) throws IllegalArgumentException {
        validate(redeemTtl, pointClaimableDuration, pointUsableDuration, distributionMethod, rewardClaimMethods);

        this.redeemTtl = redeemTtl;
        this.pointClaimableDuration = pointClaimableDuration;
        this.pointUsableDuration = pointUsableDuration;
        this.distributionMethod = distributionMethod;
        this.rewardClaimMethods = rewardClaimMethods;
        this.pointGeneration = pointGeneration;
    }

    private static void validate(int redeemTtl, int pointClaimableDuration, int pointUsableDuration, String distributionMethod, Set<String> rewardClaimMethods) throws IllegalArgumentException {
        MessageBuilder mb = new MessageBuilder();

        if (redeemTtl < 1) mb.append("Redeem TTL must be larger than 0");

        if (pointClaimableDuration < 1) mb.append("Point Claimable Duration must be larger than 0");

        if (pointUsableDuration < 1) mb.append("Point Usable Duration must be larger than 0");

        if (!distributionMethodIsValid(distributionMethod)) mb.append("Invalid Distribution Method");

        if (rewardClaimMethods.isEmpty()) mb.append("Reward Claim Methods must not be empty");
        else if (rewardClaimMethodsIsValid(rewardClaimMethods))
            mb.append(String.format("Invalid Reward Claim Methods (Valid: %s, %s).", RewardClaimMethod.BY_CUSTOMER, RewardClaimMethod.BY_MERCHANT));

        if (!mb.isEmpty()) throw new IllegalArgumentException(mb.toString());
    }

    private static boolean distributionMethodIsValid(String distributionMethod) {
        return distributionMethod.equalsIgnoreCase(DistributionMethod.POINT_CODE_GENERATION) ||
            distributionMethod.equalsIgnoreCase(DistributionMethod.CUSTOMER_CODE_SCANNING);
    }

    private static boolean rewardClaimMethodsIsValid(Set<String> rewardClaimMethods) {
        return rewardClaimMethods.size() != rewardClaimMethods.stream()
            .filter(method -> method.equalsIgnoreCase(RewardClaimMethod.BY_CUSTOMER) || method.equalsIgnoreCase(RewardClaimMethod.BY_MERCHANT))
            .collect(Collectors.toSet())
            .size();
    }

    public static class DistributionMethod {
        public static final String POINT_CODE_GENERATION = "generate";
        public static final String CUSTOMER_CODE_SCANNING = "scan";
    }

    public static class RewardClaimMethod {
        public static final String BY_CUSTOMER = "customer";
        public static final String BY_MERCHANT = "merchant";
    }
}
