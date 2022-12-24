package com.mauntung.mauntung.application.service;

import com.mauntung.mauntung.application.exception.MerchantNotFoundException;
import com.mauntung.mauntung.application.exception.RewardNotFoundException;
import com.mauntung.mauntung.application.exception.TierNotFoundException;
import com.mauntung.mauntung.application.port.membership.*;
import com.mauntung.mauntung.application.port.merchant.MerchantRepository;
import com.mauntung.mauntung.application.port.reward.RewardRepository;
import com.mauntung.mauntung.application.port.tier.TierRepository;
import com.mauntung.mauntung.domain.model.membership.*;
import com.mauntung.mauntung.domain.model.merchant.Merchant;
import com.mauntung.mauntung.domain.model.reward.Reward;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.Set;

@Component
@RequiredArgsConstructor
public class CreatePointMembershipService implements CreatePointMembershipUseCase {
    private final MerchantRepository merchantRepository;
    private final RewardRepository rewardRepository;
    private final TierRepository tierRepository;
    private final MembershipRepository membershipRepository;
    private final PointMembershipFactory membershipFactory = new PointMembershipFactoryImpl();

    @Override
    public CreatePointMembershipResponse apply(CreatePointMembershipCommand command) throws MerchantNotFoundException, RewardNotFoundException, TierNotFoundException, IllegalArgumentException {
        Merchant merchant = findMerchantByUserId(command.getUserId());

        if (isCreatedMembership(merchant.getId())) {
            throw new IllegalStateException("Already created membership");
        }

        Set<Reward> rewards = findAllRewardsByIds(command.getRewardIds());

        Set<Tier> tiers = null;
        boolean hasTiers = command.getTierIds() != null;
        if (hasTiers) {
            tiers = findAllTiersByIds(command.getTierIds());
        }

        PointMembership membership = buildPointMembership(command, merchant, rewards, tiers);
        Long membershipId = saveMembershipAndGetId(membership);

        attachRewardsToMembership(rewards, membershipId);

        if (hasTiers) attachTiersToMembership(tiers, membershipId);

        return buildResponse(membershipId, membership);
    }

    private boolean isCreatedMembership(long merchantId) {
        return membershipRepository.isExistsByMerchantId(merchantId);
    }

    private Merchant findMerchantByUserId(long userId) throws MerchantNotFoundException {
        return merchantRepository.findByUserId(userId).orElseThrow(MerchantNotFoundException::new);
    }

    private Set<Reward> findAllRewardsByIds(Set<Long> ids) throws RewardNotFoundException {
        Set<Reward> rewards = rewardRepository.findAllById(ids);
        if (rewards.size() != ids.size())
            throw new RewardNotFoundException();
        return rewards;
    }

    private Set<Tier> findAllTiersByIds(Set<Long> ids) throws TierNotFoundException {
        Set<Tier> tiers = tierRepository.findAllById(ids);
        if (tiers.size() != ids.size())
            throw new TierNotFoundException();
        return tiers;
    }

    private Long saveMembershipAndGetId(PointMembership membership) throws RuntimeException {
        return membershipRepository.save(membership).orElseThrow(() -> new RuntimeException("Can't Create Point Membership"));
    }

    private void attachRewardsToMembership(Set<Reward> rewards, long membershipId) {
        rewardRepository.attachToMembership(rewards, membershipId);
    }

    private void attachTiersToMembership(Set<Tier> tiers, long membershipId) {
        tierRepository.attachToMembership(tiers, membershipId);
    }

    private CreatePointMembershipResponse buildResponse(long membershipId, PointMembership membership) {
        return new CreatePointMembershipResponse(
            membershipId,
            membership.getName(),
            membership.getRewardsQty(),
            membership.getTiersQty(),
            membership.getCreatedAt()
        );
    }

    private PointMembership buildPointMembership(CreatePointMembershipCommand command, Merchant merchant, Set<Reward> rewards, Set<Tier> tiers) {
        return membershipFactory.builder(
            command.getName(),
            merchant,
            rewards,
            new Date(),
            command.getRules()
        )
            .tiers(tiers)
            .build();
    }
}
