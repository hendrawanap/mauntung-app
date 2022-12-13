package com.mauntung.mauntung.domain.model.point;

import java.util.Date;

public interface PointBuilder {
    Point build() throws IllegalArgumentException;

    PointBuilder id(Long id);

    PointBuilder claimedValue(Integer claimedValue);

    PointBuilder currentValue(Integer currentValue);

    PointBuilder claimedAt(Date claimedAt);
}
