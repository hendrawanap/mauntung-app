package com.mauntung.mauntung.domain.model.reward;

import java.util.Date;

public interface RewardBuilder {
    Reward build() throws IllegalArgumentException;

    RewardBuilder id(Long id);

    RewardBuilder imgUrl(String imgUrl);

    RewardBuilder stock(Integer stock);

    RewardBuilder startPeriod(Date startPeriod);

    RewardBuilder endPeriod(Date endPeriod);
}
