package com.mauntung.mauntung.domain.model.point;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Date;

@Getter
@RequiredArgsConstructor
public class ClaimedValue {
    private final int claimedValue;
    private final Date claimedAt;
}
