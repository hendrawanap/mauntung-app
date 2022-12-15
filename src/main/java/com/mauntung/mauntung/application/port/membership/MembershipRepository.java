package com.mauntung.mauntung.application.port.membership;

import com.mauntung.mauntung.domain.model.membership.Membership;

import java.util.Optional;

public interface MembershipRepository {
    Optional<Long> save(Membership membership);
}
