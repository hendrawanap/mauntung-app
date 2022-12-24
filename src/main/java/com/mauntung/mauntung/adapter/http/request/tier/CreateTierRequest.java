package com.mauntung.mauntung.adapter.http.request.tier;

import lombok.Data;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Set;

@Data
public class CreateTierRequest {
    @NotBlank(message = "Name must not be blank")
    private String name;

    @NotNull(message = "Reward Ids must not be null")
    private Set<Long> rewardIds;

    @NotNull(message = "Required Points must not be null")
    @Min(value = 0, message = "Required Points must not be negative integer")
    private Integer requiredPoints;

    @Min(value = 0, message = "Required Points must not be negative number")
    private Float multiplierFactor;
}
