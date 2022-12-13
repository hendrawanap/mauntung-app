package com.mauntung.mauntung.domain.model.point;

import java.util.Date;
import java.util.UUID;

public interface PointFactory {
    PointBuilder builder(int baseValue, int claimableDuration, int usableDuration, Date createdAt, UUID code);

    Point createCopyWithNewCurrentValue(Point oldPoint, int newCurrentValue);
}
