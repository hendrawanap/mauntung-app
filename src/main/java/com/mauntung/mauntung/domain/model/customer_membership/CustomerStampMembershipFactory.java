package com.mauntung.mauntung.domain.model.customer_membership;

import com.mauntung.mauntung.domain.model.merchant.Merchant;
import com.mauntung.mauntung.domain.model.redeem.Redeem;
import com.mauntung.mauntung.domain.model.stamp.Stamp;

import java.util.Date;
import java.util.List;
import java.util.Set;

public interface CustomerStampMembershipFactory {
    CustomerStampMembershipBuilder builder(Merchant merchant, Date joinedAt, Set<Redeem> redeems, List<Stamp> stamps);
}
