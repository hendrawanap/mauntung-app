package com.mauntung.mauntung.domain.model.redeem;

import java.util.Date;

public interface RedeemBuilder {
    Redeem build() throws IllegalArgumentException;

    RedeemBuilder id(Long id);

    RedeemBuilder usedAt(Date usedAt);
}
