package com.mauntung.mauntung.adapter.http.response.tier;

import com.mauntung.mauntung.application.port.tier.CreateTierResponse;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
public class CreateTierResponseBody {
    private final Data data;

    public CreateTierResponseBody(CreateTierResponse response) {
        data = new Data(
            response.getId(),
            response.getName(),
            response.getRewardsQty(),
            response.getRequiredPoints(),
            response.getMultiplierFactor()
        );
    }

    @Getter
    @RequiredArgsConstructor
    public static class Data {
        private final Long id;
        private final String name;
        private final Integer rewardsQty;
        private final Integer requiredPoints;
        private final Float multiplierFactor;
    }
}
