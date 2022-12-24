package com.mauntung.mauntung.adapter.persistence.reward;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;

public interface JpaRewardRepository extends CrudRepository<RewardEntity, Long> {
    @Transactional
    @Modifying
    @Query("update RewardEntity r set r.membership.id = :membershipId where r.id in :ids")
    void attachToMembership(@Param("membershipId") Long membershipId, @Param("ids") Collection<Long> ids);
}
