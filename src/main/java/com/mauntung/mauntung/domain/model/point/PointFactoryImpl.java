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
        ClaimedValue claimedValue = null;
        if (oldPoint.isClaimed()) {
            claimedValue = new ClaimedValue(oldPoint.getClaimedValue(), oldPoint.getClaimedAt());
        }

        return new BuilderImpl(
            oldPoint.getBaseValue(),
            oldPoint.getClaimableDuration(),
            oldPoint.getUsableDuration(),
            oldPoint.getCreatedAt(),
            oldPoint.getCode()
        )
            .id(oldPoint.getId())
            .currentValue(newCurrentValue)
            .claimedValue(claimedValue)
            .build();
    }

    @RequiredArgsConstructor
    private static class BuilderImpl implements PointBuilder {
        private Long id;
        private final int baseValue;
        private ClaimedValue claimedValue;
        private Integer currentValue;
        private final int claimableDuration;
        private final int usableDuration;
        private final Date createdAt;
        private final UUID code;

        @Override
        public Point build() throws IllegalArgumentException {
            validate();

            Integer claimedValue = null;
            Date claimedAt = null;
            boolean hasClaimedValue = this.claimedValue != null;
            if (hasClaimedValue) {
                claimedValue = this.claimedValue.getClaimedValue();
                claimedAt = this.claimedValue.getClaimedAt();
            }

            return new Point(id, baseValue, claimedValue, currentValue, claimableDuration, usableDuration, claimedAt, createdAt, code);
        }

        @Override
        public PointBuilder id(Long id) {
            this.id = id;
            return this;
        }

        @Override
        public PointBuilder currentValue(Integer currentValue) {
            this.currentValue = currentValue;
            return this;
        }

        @Override
        public PointBuilder claimedValue(ClaimedValue claimedValue) {
            this.claimedValue = claimedValue;
            return this;
        }

        private void validate() throws IllegalArgumentException {
            MessageBuilder mb = new MessageBuilder();

            if (baseValue < 1)
                mb.append("Base Value must not be less than 1");

            if (claimableDuration < 1)
                mb.append("Claimable Duration must not be less than 1");

            if (usableDuration < 1)
                mb.append("Usable Duration must not be less than 1");

            if (claimedValue != null && currentValue == null)
                mb.append("Current Value must not be null when Claimed Value is not null");

            if (currentValue != null && claimedValue == null)
                mb.append("Claimed Value must not be null when Current Value is not null");

            if (claimedValue != null && claimedValue.getClaimedValue() < 1)
                mb.append("Claimed Value must not be less than 1");

            if (currentValue != null && currentValue < 0)
                mb.append("Current Value must not be negative");

            if (!mb.isEmpty())
                throw new IllegalArgumentException(mb.toString());
        }
    }
}
