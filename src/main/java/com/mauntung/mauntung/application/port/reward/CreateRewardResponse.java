package com.mauntung.mauntung.application.port.reward;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Date;

@Getter
@RequiredArgsConstructor
public class CreateRewardResponse {
    private final Long id;
    private final Date createdAt;
    private final String message;
}
