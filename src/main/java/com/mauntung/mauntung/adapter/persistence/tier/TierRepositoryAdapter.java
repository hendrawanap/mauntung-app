package com.mauntung.mauntung.adapter.persistence.tier;

import com.mauntung.mauntung.application.port.tier.TierRepository;
import com.mauntung.mauntung.domain.model.membership.Tier;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Repository
@RequiredArgsConstructor
public class TierRepositoryAdapter implements TierRepository {
    private final JpaTierRepository jpaRepository;
    private final TierMapper mapper = new TierMapper();

    @Override
    public Optional<Long> save(Tier tier) {
        TierEntity entity = mapper.modelToEntity(tier);
        try {
            entity = jpaRepository.save(entity);
        } catch (IllegalStateException ex) {
            return Optional.empty();
        }
        return Optional.of(entity.getId());
    }

    @Override
    public Set<Tier> findAllById(Set<Long> ids) {
        Iterable<TierEntity> tierEntities = jpaRepository.findAllById(ids);
        return StreamSupport.stream(tierEntities.spliterator(), false)
            .map(mapper::entityToModel)
            .collect(Collectors.toSet());
    }

    @Override
    public void attachToMembership(Set<Tier> tiers, Long membershipId) {
        Set<Long> tierIds = tiers.stream().map(Tier::getId).collect(Collectors.toSet());
        jpaRepository.attachToMembership(membershipId, tierIds);
    }
}
