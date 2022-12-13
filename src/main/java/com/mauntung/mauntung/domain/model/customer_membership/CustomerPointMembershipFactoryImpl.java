package com.mauntung.mauntung.domain.model.customer_membership;

import com.mauntung.mauntung.domain.model.membership.Membership;
import com.mauntung.mauntung.domain.model.membership.Tier;
import com.mauntung.mauntung.domain.model.point.Point;
import com.mauntung.mauntung.domain.model.redeem.Redeem;
import lombok.RequiredArgsConstructor;

import java.util.Date;
import java.util.List;
import java.util.Set;

public class CustomerPointMembershipFactoryImpl implements CustomerPointMembershipFactory {
    @Override
    public CustomerPointMembershipBuilder builder(Membership membership, Date joinedAt, Set<Redeem> redeems, List<Point> points) {
        return new BuilderImpl(membership, joinedAt, redeems, points);
    }

    @RequiredArgsConstructor
    private static class BuilderImpl implements CustomerPointMembershipBuilder {
        private final Membership membership;
        private final Date joinedAt;
        private final Set<Redeem> redeems;
        private final List<Point> points;
        private Set<Tier> tiers;

        @Override
        public CustomerPointMembership build() {
            Long membershipId = membership.getId();
            String membershipName = membership.getName();
            String merchantName = membership.getMerchant().getName();
            String membershipImg = membership.getImg();
            return new CustomerPointMembership(membershipId, membershipName, merchantName, joinedAt, membershipImg, redeems, points, tiers);
        }

        @Override
        public CustomerPointMembershipBuilder tiers(Set<Tier> tiers) {
            this.tiers = tiers;
            return this;
        }
    }
}
