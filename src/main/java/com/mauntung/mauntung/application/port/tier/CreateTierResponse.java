package com.mauntung.mauntung.application.port.tier;

import com.mauntung.mauntung.domain.model.membership.Tier;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
public class CreateTierResponse {
    @Setter private SuccessResponse successResponse;
    private ErrorResponse errorResponse;

    public void setErrorResponse(String message) {
        errorResponse = new ErrorResponse(message);
    }

    @Getter
    public static class SuccessResponse {
        private final Long id;
        private final String name;
        private final Integer rewardsQty;
        private final Integer requiredPoints;
        private final Float multiplierFactor;

        public SuccessResponse(Tier tier, Long id) {
            this.id = id;
            this.name = tier.getName();
            this.rewardsQty = tier.getRewards().size();
            this.requiredPoints = tier.getRequiredPoints();
            this.multiplierFactor = tier.getMultiplierFactor();
        }
    }

    @Getter
    @RequiredArgsConstructor
    public static class ErrorResponse {
        private final String message;
    }
}
