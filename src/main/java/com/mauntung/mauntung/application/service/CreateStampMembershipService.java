package com.mauntung.mauntung.application.service;

import com.mauntung.mauntung.application.exception.MerchantNotFoundException;
import com.mauntung.mauntung.application.exception.RewardNotFoundException;
import com.mauntung.mauntung.application.port.membership.CreateStampMembershipCommand;
import com.mauntung.mauntung.application.port.membership.CreateStampMembershipResponse;
import com.mauntung.mauntung.application.port.membership.CreateStampMembershipUseCase;
import com.mauntung.mauntung.application.port.membership.MembershipRepository;
import com.mauntung.mauntung.application.port.merchant.MerchantRepository;
import com.mauntung.mauntung.application.port.reward.RewardRepository;
import com.mauntung.mauntung.domain.model.membership.StampMembership;
import com.mauntung.mauntung.domain.model.membership.StampMembershipFactory;
import com.mauntung.mauntung.domain.model.membership.StampMembershipFactoryImpl;
import com.mauntung.mauntung.domain.model.membership.StampRules;
import com.mauntung.mauntung.domain.model.merchant.Merchant;
import com.mauntung.mauntung.domain.model.reward.Reward;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class CreateStampMembershipService implements CreateStampMembershipUseCase {
    private final MerchantRepository merchantRepository;
    private final RewardRepository rewardRepository;
    private final MembershipRepository membershipRepository;
    private final StampMembershipFactory membershipFactory = new StampMembershipFactoryImpl();

    @Override
    public CreateStampMembershipResponse apply(CreateStampMembershipCommand command) {
        Merchant merchant = findMerchantByUserId(command.getUserId());

        if (isCreatedMembership(merchant.getId())) {
            throw new IllegalStateException("Already created membership");
        }

        Set<Reward> rewards = findAllRewardsByIds(command.getRewardIds());

        StampMembership membership = buildStampMembership(command, merchant, rewards);
        Long membershipId = saveMembershipAndGetId(membership);

        attachRewardsToMembership(rewards, membershipId);

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

    private Long saveMembershipAndGetId(StampMembership membership) throws RuntimeException {
        return membershipRepository.save(membership).orElseThrow(() -> new RuntimeException("Can't Create Stamp Membership"));
    }

    private void attachRewardsToMembership(Set<Reward> rewards, long membershipId) {
        rewardRepository.attachToMembership(rewards, membershipId);
    }

    private CreateStampMembershipResponse buildResponse(long membershipId, StampMembership membership) {
        return new CreateStampMembershipResponse(
            membershipId,
            membership.getName(),
            membership.getRewardsQty(),
            membership.getRules().getCardCapacity(),
            membership.getCreatedAt()
        );
    }

    private StampMembership buildStampMembership(CreateStampMembershipCommand command, Merchant merchant, Set<Reward> rewards) throws IllegalArgumentException {
        return membershipFactory.builder(
            command.getName(),
            merchant,
            rewards,
            new Date(),
            new StampRules(command.getRedeemTtl(), command.getUsableDuration(), command.getCardCapacity())
        ).build();
    }
}
