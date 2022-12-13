package com.mauntung.mauntung.domain.model.customer_membership;

import com.mauntung.mauntung.domain.model.redeem.Redeem;

import java.util.Date;
import java.util.Set;

public interface CustomerMembership {
    Long getId();

    String getName();

    String getMerchantName();

    Date getJoinedAt();

    String getImg();

    Set<Redeem> getRedeems();

    int getBalance();
}
