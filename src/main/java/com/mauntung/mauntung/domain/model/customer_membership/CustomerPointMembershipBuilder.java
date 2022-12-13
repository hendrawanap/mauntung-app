package com.mauntung.mauntung.domain.model.customer_membership;

import com.mauntung.mauntung.domain.model.membership.Tier;

import java.util.Set;

public interface CustomerPointMembershipBuilder {
    CustomerPointMembership build();

    CustomerPointMembershipBuilder tiers(Set<Tier> tiers);
}
