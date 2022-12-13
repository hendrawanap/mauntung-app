package com.mauntung.mauntung.domain.model.point;

import com.mauntung.mauntung.domain.common.MessageBuilder;
import lombok.RequiredArgsConstructor;

import java.util.Date;
import java.util.UUID;

public class PointFactoryImpl implements PointFactory {
    @Override
    public PointBuilder builder(int baseValue, int claimableDuration, int usableDuration, Date createdAt, UUID code) {
        return new BuilderImpl(baseValue, claimableDuration, usableDuration, createdAt, code);
    }

    @Override
    public Point createCopyWithNewCurrentValue(Point oldPoint, int newCurrentValue) {
        return new BuilderImpl(
            oldPoint.getBaseValue(),
            oldPoint.getClaimableDuration(),
            oldPoint.getUsableDuration(),
            oldPoint.getCreatedAt(),
            oldPoint.getCode()
        )
            .id(oldPoint.getId())
            .claimedValue(oldPoint.getClaimedValue())
            .currentValue(newCurrentValue)
            .claimedAt(oldPoint.getClaimedAt())
            .build();
    }

    @RequiredArgsConstructor
    private static class BuilderImpl implements PointBuilder {
        private Long id;
        private final int baseValue;
        private Integer claimedValue;
        private Integer currentValue;
        private final int claimableDuration;
        private final int usableDuration;
        private Date claimedAt;
        private final Date createdAt;
        private final UUID code;

        @Override
        public Point build() throws IllegalArgumentException {
            validate();
            return new Point(id, baseValue, claimedValue, currentValue, claimableDuration, usableDuration, claimedAt, createdAt, code);
        }

        @Override
        public PointBuilder id(Long id) {
            this.id = id;
            return this;
        }

        @Override
        public PointBuilder claimedValue(Integer claimedValue) {
            this.claimedValue = claimedValue;
            return this;
        }

        @Override
        public PointBuilder currentValue(Integer currentValue) {
            this.currentValue = currentValue;
            return this;
        }

        @Override
        public PointBuilder claimedAt(Date claimedAt) {
            this.claimedAt = claimedAt;
            return this;
        }

        private void validate() throws IllegalArgumentException {
            MessageBuilder mb = new MessageBuilder();

            if (baseValue < 1) mb.append("Base Value must not be less than 1");

            if (claimableDuration < 1) mb.append("Claimable Duration must not be less than 1");

            if (usableDuration < 1) mb.append("Usable Duration must not be less than 1");

            if (claimedValue != null && claimedValue < 1) mb.append("Claimed Value must not be less than 1");

            if (currentValue != null && currentValue < 0) mb.append("Current Value must not be negative");

            if (mb.length() > 0) throw new IllegalArgumentException(mb.toString());
        }
    }
}
