package com.mauntung.mauntung.domain.model.membership;

import com.mauntung.mauntung.domain.common.MessageBuilder;
import lombok.Getter;

@Getter
public class PointGeneration {
    private final Type type;
    private final int points;
    private final int divider;

    public PointGeneration(Type type, int points, int divider) throws IllegalArgumentException {
        validate(points, divider);
        this.type = type;
        this.points = points;
        this.divider = type == Type.FIXED ? 1 : divider;
    }

    private static void validate(int points, int divider) throws IllegalArgumentException {
        MessageBuilder mb = new MessageBuilder();

        if (points < 1) mb.append("Points must be larger than 0");

        if (divider < 1) mb.append("Divider must be larger than 0");

        if (!mb.isEmpty()) throw new IllegalArgumentException(mb.toString());
    }

    public enum Type {
        NOMINAL,
        ITEM,
        FIXED;

        @Override
        public String toString() {
            return this.name().toLowerCase();
        }
    }
}
