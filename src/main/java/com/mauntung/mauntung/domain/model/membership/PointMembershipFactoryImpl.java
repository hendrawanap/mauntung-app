package com.mauntung.mauntung.domain.model.membership;

import com.mauntung.mauntung.domain.common.MessageBuilder;
import com.mauntung.mauntung.domain.model.reward.Reward;
import lombok.RequiredArgsConstructor;

import java.util.Date;
import java.util.Set;
import java.util.stream.Collectors;

public class PointMembershipFactoryImpl implements PointMembershipFactory {
    @Override
    public PointMembershipBuilder builder(String name, Set<Reward> rewards, Date createdAt, PointRules rules, boolean isFinalized) {
        return new BuilderImpl(name, rewards, createdAt, rules, isFinalized);
    }

    @RequiredArgsConstructor
    private static class BuilderImpl implements PointMembershipBuilder {
        private Long id;
        private final String name;
        private final Set<Reward> rewards;
        private final Date createdAt;
        private final PointRules rules;
        private String img;
        private Set<Tier> tiers;
        private final boolean isFinalized;

        @Override
        public PointMembership build() throws IllegalArgumentException {
            validateTiers();
            return new PointMembership(id, name, rewards, createdAt, rules, img, tiers, isFinalized);
        }

        @Override
        public PointMembershipBuilder id(Long id) {
            this.id = id;
            return this;
        }

        @Override
        public PointMembershipBuilder img(String img) {
            this.img = img;
            return this;
        }

        @Override
        public PointMembershipBuilder tiers(Set<Tier> tiers) {
            this.tiers = tiers;
            return this;
        }

        private void validateTiers() throws IllegalArgumentException {
            if (tiers == null) return;

            boolean hasBaseTier = tiers.stream().anyMatch(tier -> tier.getRequiredPoints() == 0);
            boolean hasMoreThanOneTiers = tiers.size() > 1;
            boolean hasDuplicateRequiredPoints = tiers.size() != tiers.stream()
                .map(Tier::getRequiredPoints)
                .collect(Collectors.toSet())
                .size();

            MessageBuilder mb = new MessageBuilder();

            if (!hasBaseTier) mb.append("One tier with 0 required points is required");

            if (!hasMoreThanOneTiers) mb.append("Two or more tiers are required");

            if (hasDuplicateRequiredPoints) mb.append("Tiers must not have duplicate required points");

            if (!mb.isEmpty()) throw new IllegalArgumentException(mb.toString());
        }
    }
}
