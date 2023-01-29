package com.mauntung.mauntung.domain.model.membership;

import com.mauntung.mauntung.domain.model.reward.Reward;

import java.util.Date;
import java.util.Set;

public interface Membership {
    String DUMMY_MEMBERSHIP_IMG = "https://via.placeholder.com/120";

    Long getId();

    String getName();

    Set<Reward> getRewards();

    Date getCreatedAt();

    String getImg();

    Integer getRewardsQty();

    boolean isFinalized();

    void confirmFinalize();

    enum Type {
        POINT,
        STAMP
    }
}
