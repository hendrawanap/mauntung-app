package com.mauntung.mauntung.adapter.persistence.membership;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.mauntung.mauntung.application.port.membership.MembershipRepository;
import com.mauntung.mauntung.domain.model.membership.Membership;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class MembershipRepositoryImpl implements MembershipRepository {
    private final JpaMembershipRepository jpaRepository;
    private final MembershipMapper mapper = new MembershipMapper();

    @Override
    public Optional<Long> save(Membership membership) {
        MembershipEntity entity;

        try {
            entity = mapper.modelToEntity(membership);
        } catch (JsonProcessingException ex) {
            return Optional.empty();
        }

        try {
            entity = jpaRepository.save(entity);
        } catch (IllegalStateException ex) {
            return Optional.empty();
        }

        return Optional.of(entity.getId());
    }
}
