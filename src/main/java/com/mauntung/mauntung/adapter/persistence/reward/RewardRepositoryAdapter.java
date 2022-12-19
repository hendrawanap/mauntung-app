package com.mauntung.mauntung.adapter.persistence.reward;

import com.mauntung.mauntung.application.port.reward.RewardRepository;
import com.mauntung.mauntung.domain.model.reward.Reward;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Component
@RequiredArgsConstructor
public class RewardRepositoryAdapter implements RewardRepository {
    private final JpaRewardRepository jpaRepository;
    private final RewardMapper mapper = new RewardMapper();

    @Override
    public Optional<Long> save(Reward reward) {
        RewardEntity entity = mapper.modelToEntity(reward);
        try {
            entity = jpaRepository.save(entity);
        } catch (IllegalStateException ex) {
            return Optional.empty();
        }
        return Optional.of(entity.getId());
    }

    @Override
    public Set<Reward> findAllById(Set<Long> ids) {
        Iterable<RewardEntity> rewardEntities = jpaRepository.findAllById(ids);
        return StreamSupport.stream(rewardEntities.spliterator(), false)
            .map(mapper::entityToModel)
            .collect(Collectors.toSet());
    }

    @Override
    public void attachToMembership(Set<Reward> rewards, Long membershipId) {
        Set<Long> rewardIds = rewards.stream().map(Reward::getId).collect(Collectors.toSet());
        jpaRepository.attachToMembership(membershipId, rewardIds);
    }
}
