package com.mauntung.mauntung.adapter.http.request.membership;

import com.mauntung.mauntung.domain.model.membership.PointRules;
import com.mauntung.mauntung.domain.model.membership.StampRules;
import lombok.Data;

@Data
public class Rules {
    private PointRules point;
    private StampRules stamp;
}
