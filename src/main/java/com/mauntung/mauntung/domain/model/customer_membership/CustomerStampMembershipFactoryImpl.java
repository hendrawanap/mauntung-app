package com.mauntung.mauntung.domain.model.customer_membership;

import com.mauntung.mauntung.domain.model.membership.StampMembership;
import com.mauntung.mauntung.domain.model.redeem.Redeem;
import com.mauntung.mauntung.domain.model.stamp.Stamp;
import lombok.RequiredArgsConstructor;

import java.util.Date;
import java.util.List;
import java.util.Set;

public class CustomerStampMembershipFactoryImpl implements CustomerStampMembershipFactory {
    @Override
    public CustomerStampMembershipBuilder builder(StampMembership membership, Date joinedAt, Set<Redeem> redeems, List<Stamp> stamps) {
        return new BuilderImpl(membership, joinedAt, redeems, stamps);
    }

    @RequiredArgsConstructor
    private static class BuilderImpl implements CustomerStampMembershipBuilder {
        private final StampMembership membership;
        private final Date joinedAt;
        private final Set<Redeem> redeems;
        private final List<Stamp> stamps;

        @Override
        public CustomerStampMembership build() {
            Long membershipId = membership.getId();
            String membershipName = membership.getName();
            String merchantName = membership.getMerchant().getName();
            String membershipImg = membership.getImg();
            int cardCapacity = membership.getRules().getCardCapacity();
            return new CustomerStampMembership(membershipId, membershipName, merchantName, joinedAt, membershipImg, redeems, stamps, cardCapacity);
        }
    }
}
