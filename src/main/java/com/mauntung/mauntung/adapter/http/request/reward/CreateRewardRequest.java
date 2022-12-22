package com.mauntung.mauntung.adapter.http.request.reward;

import lombok.Data;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
public class CreateRewardRequest {
    @NotBlank
    private String name;

    @NotBlank
    private String description;

    @NotBlank
    private String termsCondition;

    @Min(0)
    @NotNull
    private Integer cost;

    @Min(0)
    private Integer stock;

    private String startPeriod;

    private String endPeriod;
}
