package com.mauntung.mauntung.adapter.http.request.reward;

import lombok.Data;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
public class CreateRewardRequest {
    @NotBlank(message = "Name must not be blank")
    private String name;

    @NotBlank(message = "Description must not be blank")
    private String description;

    @NotBlank(message = "Terms Condition must not be blank")
    private String termsCondition;

    @Min(value = 0, message = "Cost must be larger than 0")
    @NotNull(message = "Cost must not be null")
    private Integer cost;

    @Min(value = 0, message = "Stock must be larger than 0")
    private Integer stock;

    private String startPeriod;

    private String endPeriod;
}
