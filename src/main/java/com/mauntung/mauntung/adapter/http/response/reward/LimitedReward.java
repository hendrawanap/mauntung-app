package com.mauntung.mauntung.adapter.http.response.reward;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class LimitedReward {
    private final long id;
    private final String name;
    private final long cost;
    private final String img;
}
