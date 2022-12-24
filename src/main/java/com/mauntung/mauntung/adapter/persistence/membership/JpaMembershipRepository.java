package com.mauntung.mauntung.adapter.persistence.membership;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

public interface JpaMembershipRepository extends CrudRepository<MembershipEntity, Long> {
    @Query("select (count(m) > 0) from MembershipEntity m where m.merchant.id = :id")
    boolean isExistsByMerchantId(@Param("id") Long id);
}
