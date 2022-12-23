package com.mauntung.mauntung.adapter.http.request.tier;

import lombok.Data;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Set;

@Data
public class CreateTierRequest {
    @NotBlank
    private String name;

    @NotNull
    private Set<Long> rewardIds;

    @NotNull
    @Min(0)
    private Integer requiredPoints;

    @Min(0)
    private Float multiplierFactor;
}
