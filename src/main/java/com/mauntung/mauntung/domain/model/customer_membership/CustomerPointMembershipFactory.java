package com.mauntung.mauntung.domain.model.customer_membership;

import com.mauntung.mauntung.domain.model.merchant.Merchant;
import com.mauntung.mauntung.domain.model.point.Point;
import com.mauntung.mauntung.domain.model.redeem.Redeem;

import java.util.Date;
import java.util.List;
import java.util.Set;

public interface CustomerPointMembershipFactory {
    CustomerPointMembershipBuilder builder(Merchant merchant, Date joinedAt, Set<Redeem> redeems, List<Point> points);
}
