package com.mauntung.mauntung.domain.model.point;

public interface PointBuilder {
    Point build() throws IllegalArgumentException;

    PointBuilder id(Long id);

    PointBuilder currentValue(Integer currentValue);

    PointBuilder claimedValue(ClaimedValue claimedValue);
}
