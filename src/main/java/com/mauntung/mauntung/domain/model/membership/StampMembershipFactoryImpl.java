package com.mauntung.mauntung.domain.model.membership;

import com.mauntung.mauntung.domain.model.merchant.Merchant;
import com.mauntung.mauntung.domain.model.reward.Reward;
import lombok.RequiredArgsConstructor;

import java.util.Date;
import java.util.Set;

public class StampMembershipFactoryImpl implements StampMembershipFactory {
    @Override
    public StampMembershipBuilder builder(String name, Merchant merchant, Set<Reward> rewards, Date createdAt, StampRules rules) {
        return new BuilderImpl(name, merchant, rewards, createdAt, rules);
    }

    @RequiredArgsConstructor
    private static class BuilderImpl implements StampMembershipBuilder {
        private Long id;
        private final String name;
        private final Merchant merchant;
        private final Set<Reward> rewards;
        private final Date createdAt;
        private String img;
        private final StampRules rules;

        @Override
        public StampMembership build() {
            return new StampMembership(id, name, merchant, rewards, createdAt, img, rules);
        }

        @Override
        public StampMembershipBuilder id(Long id) {
            this.id = id;
            return this;
        }

        @Override
        public StampMembershipBuilder img(String img) {
            this.img = img;
            return this;
        }
    }
}
