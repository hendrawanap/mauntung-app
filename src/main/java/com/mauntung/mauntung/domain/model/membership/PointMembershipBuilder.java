package com.mauntung.mauntung.domain.model.membership;

import java.util.Set;

public interface PointMembershipBuilder {
    PointMembership build() throws IllegalArgumentException;

    PointMembershipBuilder id(Long id);

    PointMembershipBuilder img(String img);

    PointMembershipBuilder tiers(Set<Tier> tiers);
}
