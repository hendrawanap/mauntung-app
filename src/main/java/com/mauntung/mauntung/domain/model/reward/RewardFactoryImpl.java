package com.mauntung.mauntung.domain.model.reward;

import com.mauntung.mauntung.domain.common.MessageBuilder;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;

import java.util.Date;

public class RewardFactoryImpl implements RewardFactory {
    @Override
    public RewardBuilder builder(String name, String description, String termsCondition, int cost) {
        return new BuilderImpl(name, description, termsCondition, cost);
    }

    @Override
    public Reward createCopyWithNewStock(Reward oldReward, int newStock) {
        return builder(
            oldReward.getName(),
            oldReward.getDescription(),
            oldReward.getTermsCondition(),
            oldReward.getCost()
        )
            .id(oldReward.getId())
            .stock(newStock)
            .startPeriod(oldReward.getStartPeriod())
            .endPeriod(oldReward.getEndPeriod())
            .build();
    }

    @RequiredArgsConstructor(access = AccessLevel.PACKAGE)
    private static class BuilderImpl implements RewardBuilder {
        private final String name;
        private final String description;
        private final String termsCondition;
        private final int cost;
        private Long id;
        private String imgUrl;
        private Integer stock;
        private Date startPeriod;
        private Date endPeriod;

        @Override
        public RewardBuilder id(Long id) {
            this.id = id;
            return this;
        }

        @Override
        public RewardBuilder imgUrl(String imgUrl) {
            this.imgUrl = imgUrl;
            return this;
        }

        @Override
        public RewardBuilder stock(Integer stock) {
            this.stock = stock;
            return this;
        }

        @Override
        public RewardBuilder startPeriod(Date startPeriod) {
            this.startPeriod = startPeriod;
            return this;
        }

        @Override
        public RewardBuilder endPeriod(Date endPeriod) {
            this.endPeriod = endPeriod;
            return this;
        }

        @Override
        public Reward build() throws IllegalArgumentException {
            validate();

            if (imgUrl == null) imgUrl = Reward.DUMMY_IMG_URL;

            return new Reward(id, name, description, termsCondition, cost, imgUrl, stock, startPeriod, endPeriod);
        }

        private void validate() throws IllegalArgumentException {
            MessageBuilder mb = new MessageBuilder();

            if (cost < 0) mb.append("Cost must be larger than or equals to 0");

            if (stock != null && stock < 0) mb.append("Stock must not be negative value");

            if ((startPeriod == null && endPeriod != null) || (startPeriod != null && endPeriod == null))
                mb.append("Start Period & End Period must be both null or both exist");
            else if (startPeriod != null && !startPeriod.before(endPeriod))
                mb.append("Start Period must be before End Period");

            if (mb.length() > 0) throw new IllegalArgumentException(mb.toString());
        }
    }
}
