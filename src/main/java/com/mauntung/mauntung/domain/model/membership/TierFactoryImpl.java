package com.mauntung.mauntung.domain.model.membership;

import com.mauntung.mauntung.domain.common.MessageBuilder;
import com.mauntung.mauntung.domain.model.reward.Reward;
import lombok.RequiredArgsConstructor;

import java.util.Set;

public class TierFactoryImpl implements TierFactory {
    @Override
    public TierBuilder builder(String name, Set<Reward> rewards, int requiredPoints) {
        return new BuilderImpl(name, rewards, requiredPoints);
    }


    @RequiredArgsConstructor
    private static class BuilderImpl implements TierBuilder {
        private Long id;
        private final String name;
        private final Set<Reward> rewards;
        private final int requiredPoints;
        private Float multiplierFactor;

        @Override
        public Tier build() {
            validate();

            if (multiplierFactor == null)
                multiplierFactor = Tier.DEFAULT_MULTIPLIER_FACTOR;

            return new Tier(id, name, rewards, requiredPoints, multiplierFactor);
        }

        @Override
        public TierBuilder id(Long id) {
            this.id = id;
            return this;
        }

        @Override
        public TierBuilder multiplierFactor(Float multiplierFactor) {
            this.multiplierFactor = multiplierFactor;
            return this;
        }

        private void validate() throws IllegalArgumentException {
            MessageBuilder mb = new MessageBuilder();

            if (requiredPoints < 0) mb.append("Required Points must not be negative value");

            if (multiplierFactor != null && multiplierFactor < 0) mb.append("Multiplier Factor must not be negative value");

            if (!mb.isEmpty()) throw new IllegalArgumentException(mb.toString());
        }
    }
}
