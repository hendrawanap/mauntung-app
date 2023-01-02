package com.mauntung.mauntung.adapter.http.response.reward;

import com.mauntung.mauntung.application.port.reward.ListMembershipRewardsResponse;
import lombok.Getter;

import java.util.Set;
import java.util.stream.Collectors;

@Getter
public class ListMembershipRewardsResponseBody {
    private final Set<LimitedReward> data;

    public ListMembershipRewardsResponseBody(ListMembershipRewardsResponse response) {
        data = response.getRewards()
            .stream()
            .map(reward -> new LimitedReward(reward.getId(), reward.getName(), reward.getCost(), reward.getImgUrl()))
            .collect(Collectors.toSet());
    }
}
