package com.mauntung.mauntung.application.port.membership;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.mauntung.mauntung.domain.model.membership.Membership;

import java.util.Optional;

public interface MembershipRepository {
    Optional<Long> save(Membership membership);

    boolean isExistsByMerchantId(Long merchantId);

    Optional<Membership> findByUserId(Long userId) throws JsonProcessingException;
}
