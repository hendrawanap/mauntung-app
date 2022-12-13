package com.mauntung.mauntung.domain.model.redeem;

import com.mauntung.mauntung.domain.model.reward.Reward;

import java.util.Date;
import java.util.UUID;

public interface RedeemFactory {
    RedeemBuilder builder(String name, String description, String termsCondition, int cost, UUID code, Reward reward, Date createdAt, Date expiredAt);
}
