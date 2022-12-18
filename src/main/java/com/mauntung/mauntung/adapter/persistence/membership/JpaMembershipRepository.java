package com.mauntung.mauntung.adapter.persistence.membership;

import org.springframework.data.repository.CrudRepository;

public interface JpaMembershipRepository extends CrudRepository<MembershipEntity, Long> {
}
