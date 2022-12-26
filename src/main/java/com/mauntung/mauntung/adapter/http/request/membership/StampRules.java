package com.mauntung.mauntung.adapter.http.request.membership;

import lombok.Data;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

@Data
public class StampRules {
    @NotNull
    @Positive
    private Integer redeemTtl;

    @NotNull
    @Positive
    private Integer usableDuration;

    @NotNull
    @Range(min = 1, max = 20)
    private Integer cardCapacity;
}
