package com.mauntung.mauntung.application.port.reward;

import com.mauntung.mauntung.domain.model.reward.Reward;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Set;

@Getter
@RequiredArgsConstructor
public class ListMembershipRewardsResponse {
    private final Set<Reward> rewards;
}
