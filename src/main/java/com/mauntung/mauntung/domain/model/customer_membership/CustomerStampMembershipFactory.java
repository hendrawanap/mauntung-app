package com.mauntung.mauntung.domain.model.customer_membership;

import com.mauntung.mauntung.domain.model.membership.StampMembership;
import com.mauntung.mauntung.domain.model.redeem.Redeem;
import com.mauntung.mauntung.domain.model.stamp.Stamp;

import java.util.Date;
import java.util.List;
import java.util.Set;

public interface CustomerStampMembershipFactory {
    CustomerStampMembershipBuilder builder(StampMembership membership, Date joinedAt, Set<Redeem> redeems, List<Stamp> stamps);
}
