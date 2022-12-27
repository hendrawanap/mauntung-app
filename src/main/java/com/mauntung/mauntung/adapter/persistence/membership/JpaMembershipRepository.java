package com.mauntung.mauntung.adapter.persistence.membership;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface JpaMembershipRepository extends CrudRepository<MembershipEntity, Long> {
    @Query("select m from MembershipEntity m where m.merchant.user.id = :userId")
    Optional<MembershipEntity> findByUserId(@Param("userId") Long userId);

    @Query("select (count(m) > 0) from MembershipEntity m where m.merchant.id = :id")
    boolean isExistsByMerchantId(@Param("id") Long id);
}
