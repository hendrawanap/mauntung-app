package com.mauntung.mauntung.application.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.mauntung.mauntung.application.exception.MembershipNotFoundException;
import com.mauntung.mauntung.application.port.membership.MembershipRepository;
import com.mauntung.mauntung.application.port.reward.ListMembershipRewardsQuery;
import com.mauntung.mauntung.application.port.reward.ListMembershipRewardsResponse;
import com.mauntung.mauntung.application.port.reward.ListMembershipRewardsUseCase;
import com.mauntung.mauntung.domain.model.membership.Membership;
import com.mauntung.mauntung.domain.model.reward.Reward;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ListMembershipRewardsService implements ListMembershipRewardsUseCase {
    private final MembershipRepository membershipRepository;

    @Override
    public ListMembershipRewardsResponse apply(ListMembershipRewardsQuery query) {
        Membership membership;
        try {
            membership = membershipRepository.findByUserId(query.getMerchantUserId()).orElseThrow();
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        } catch (NoSuchElementException ex) {
            throw new MembershipNotFoundException();
        }

        Set<Reward> filteredRewards = filterRewards(membership.getRewards(), query);
        return new ListMembershipRewardsResponse(filteredRewards);
    }

    private Set<Reward> filterRewards(Set<Reward> rewards, ListMembershipRewardsQuery query) {
        return rewards.stream()
            .filter(reward -> {
                if (query.getMinCost() == null) return true;
                return reward.getCost() >= query.getMinCost();
            })
            .filter(reward -> {
                if (query.getMaxCost() == null) return true;
                return reward.getCost() <= query.getMaxCost();
            })
            .filter(reward -> {
                if (query.getQuery() == null) return true;
                return reward.getName().contains(query.getQuery());
            })
            .collect(Collectors.toSet());
    }
}
