package com.mauntung.mauntung.adapter.http.request.membership;

import lombok.Data;

import javax.validation.Valid;

@Data
public class Rules {
    @Valid
    private PointRules point;

    @Valid
    private StampRules stamp;
}
