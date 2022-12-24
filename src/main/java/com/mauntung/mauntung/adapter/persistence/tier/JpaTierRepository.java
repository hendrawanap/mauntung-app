package com.mauntung.mauntung.adapter.persistence.tier;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;

public interface JpaTierRepository extends CrudRepository<TierEntity, Long> {
    @Transactional
    @Modifying
    @Query("update TierEntity t set t.membership.id = :membershipId where t.id in :ids and t.membership is null")
    void attachToMembership(@Param("membershipId") Long membershipId, @Param("ids") Collection<Long> ids);
}
