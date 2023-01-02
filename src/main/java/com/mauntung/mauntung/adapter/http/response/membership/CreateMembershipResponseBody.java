package com.mauntung.mauntung.adapter.http.response.membership;

import com.mauntung.mauntung.application.port.membership.CreatePointMembershipResponse;
import com.mauntung.mauntung.application.port.membership.CreateStampMembershipResponse;
import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

@Getter
public class CreateMembershipResponseBody {
    private final Map<String, Object> data = new HashMap<>();

    public CreateMembershipResponseBody(CreatePointMembershipResponse response) {
        data.put("id", response.getId());
        data.put("name", response.getName());
        data.put("rewardsQty", response.getRewardsQty());
        data.put("tiersQty", response.getTiersQty());
        data.put("createdAt", response.getCreatedAt());
    }

    public CreateMembershipResponseBody(CreateStampMembershipResponse response) {
        data.put("id", response.getId());
        data.put("name", response.getName());
        data.put("rewardsQty", response.getRewardsQty());
        data.put("cardCapacity", response.getCardCapacity());
        data.put("createdAt", response.getCreatedAt());
    }
}
