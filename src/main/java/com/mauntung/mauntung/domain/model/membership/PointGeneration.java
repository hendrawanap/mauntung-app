package com.mauntung.mauntung.domain.model.membership;

import com.mauntung.mauntung.domain.common.MessageBuilder;
import lombok.Getter;

import java.util.List;

@Getter
public class PointGeneration {
    public static final String TYPE_NOMINAL = "nominal";
    public static final String TYPE_ITEM = "item";
    public static final String TYPE_FIXED = "fixed";

    private final String type;
    private final int points;
    private final int divider;

    public PointGeneration(String type, int points, int divider) throws IllegalArgumentException {
        validate(type, points, divider);
        this.type = type;
        this.points = points;
        this.divider = type.equalsIgnoreCase(TYPE_FIXED) ? 1 : divider;
    }

    private static void validate(String type, int points, int divider) throws IllegalArgumentException {
        MessageBuilder mb = new MessageBuilder();

        if (!typeIsValid(type))
            mb.append(String.format("Invalid Type (Valid: %s, %s, %s)", TYPE_NOMINAL, TYPE_ITEM, TYPE_FIXED));

        if (points < 1) mb.append("Points must be larger than 0");

        if (divider < 1) mb.append("Divider must be larger than 0");

        if (!mb.isEmpty()) throw new IllegalArgumentException(mb.toString());
    }

    private static boolean typeIsValid(String type) {
        return List.of(TYPE_NOMINAL, TYPE_ITEM, TYPE_FIXED).contains(type);
    }
}
