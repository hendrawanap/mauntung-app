package com.mauntung.mauntung.application.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.mauntung.mauntung.application.port.membership.MembershipRepository;
import com.mauntung.mauntung.application.port.reward.CreateRewardCommand;
import com.mauntung.mauntung.application.port.reward.CreateRewardResponse;
import com.mauntung.mauntung.application.port.reward.CreateRewardUseCase;
import com.mauntung.mauntung.application.port.reward.RewardRepository;
import com.mauntung.mauntung.domain.model.membership.Membership;
import com.mauntung.mauntung.domain.model.reward.Reward;
import com.mauntung.mauntung.domain.model.reward.RewardBuilder;
import com.mauntung.mauntung.domain.model.reward.RewardFactory;
import com.mauntung.mauntung.domain.model.reward.RewardFactoryImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class CreateRewardService implements CreateRewardUseCase {
    private static final String BASIC_SUCCESS_MESSAGE = "Successfully created reward";
    private static final String ATTACHED_SUCCESS_MESSAGE = "Successfully created reward and attached to membership";
    private static final String ATTACHED_FAILURE_MESSAGE = "Successfully created reward but failed in attaching to membership";
    private static final String FIND_MEMBERSHIP_FAILURE_MESSAGE = "Successfully created reward but failed in finding membership";

    private final RewardRepository rewardRepository;
    private final MembershipRepository membershipRepository;
    private final RewardFactory rewardFactory = new RewardFactoryImpl();

    @Override
    public CreateRewardResponse apply(CreateRewardCommand command) {
        Reward reward = buildReward(command);
        Long rewardId = saveRewardAndGetId(reward);
        String responseMessage = BASIC_SUCCESS_MESSAGE;

        try {
            Optional<Long> membershipId = getMerchantMembershipIdByUserId(command.getUserId());
            boolean hasMembership = membershipId.isPresent();

            if (hasMembership) {
                attachSavedRewardToMembership(command, rewardId, membershipId.get());
                responseMessage = ATTACHED_SUCCESS_MESSAGE;
            }
        } catch (JsonProcessingException ex) {
            responseMessage = FIND_MEMBERSHIP_FAILURE_MESSAGE;
        } catch (Exception ex) {
            responseMessage = ATTACHED_FAILURE_MESSAGE;
        }

        return buildResponse(rewardId, responseMessage);
    }

    private Optional<Long> getMerchantMembershipIdByUserId(Long userId) throws JsonProcessingException {
        Membership membership;

        try {
            membership = membershipRepository.findByUserId(userId).orElseThrow();
        } catch (NoSuchElementException ex) {
            return Optional.empty();
        }

        return Optional.of(membership.getId());
    }

    private void attachSavedRewardToMembership(CreateRewardCommand command, long rewardId, long membershipId) {
        Reward reward = buildReward(command, rewardId);
        rewardRepository.attachToMembership(Set.of(reward), membershipId);
    }

    private Long saveRewardAndGetId(Reward reward) throws IllegalArgumentException {
        return rewardRepository.save(reward).orElseThrow(() -> new RuntimeException("Can't Create Reward"));
    }

    private CreateRewardResponse buildResponse(long rewardId, String message) {
        return new CreateRewardResponse(rewardId, new Date(), message);
    }

    private RewardBuilder rewardBuilderFromCommand(CreateRewardCommand command) throws IllegalArgumentException {
        return rewardFactory.builder(
                command.getName(),
                command.getDescription(),
                command.getTermsCondition(),
                command.getCost()
            )
            .startPeriod(command.getStartPeriod())
            .endPeriod(command.getEndPeriod())
            .stock(command.getStock());
    }

    private Reward buildReward(CreateRewardCommand command) throws IllegalArgumentException {
        return rewardBuilderFromCommand(command).build();
    }

    private Reward buildReward(CreateRewardCommand command, Long id) throws IllegalArgumentException {
        return rewardBuilderFromCommand(command).id(id).build();
    }
}
