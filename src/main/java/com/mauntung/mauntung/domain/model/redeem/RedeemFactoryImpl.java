package com.mauntung.mauntung.domain.model.redeem;

import com.mauntung.mauntung.domain.common.MessageBuilder;
import com.mauntung.mauntung.domain.model.reward.Reward;
import lombok.RequiredArgsConstructor;

import java.util.Date;
import java.util.UUID;

public class RedeemFactoryImpl implements RedeemFactory {
    @Override
    public RedeemBuilder builder(String name, String description, String termsCondition, int cost, UUID code, Reward reward, Date createdAt, Date expiredAt) {
        return new BuilderImpl(name, description, termsCondition, cost, code, reward, createdAt, expiredAt);
    }

    @RequiredArgsConstructor
    private static class BuilderImpl implements RedeemBuilder {
        private Long id;
        private final String name;
        private final String description;
        private final String termsCondition;
        private final int cost;
        private final UUID code;
        private final Reward reward;
        private final Date createdAt;
        private final Date expiredAt;
        private Date usedAt;

        @Override
        public Redeem build() throws IllegalArgumentException {
            validate();
            return new Redeem(id, name, description, termsCondition, cost, code, reward, createdAt, expiredAt, usedAt);
        }

        @Override
        public RedeemBuilder id(Long id) {
            this.id = id;
            return this;
        }

        @Override
        public RedeemBuilder usedAt(Date usedAt) {
            this.usedAt = usedAt;
            return this;
        }

        private void validate() throws IllegalArgumentException {
            MessageBuilder mb = new MessageBuilder();

            if (cost < 0) mb.append("Cost must not be negative");

            if (!mb.isEmpty()) throw new IllegalArgumentException(mb.toString());
        }
    }
}
