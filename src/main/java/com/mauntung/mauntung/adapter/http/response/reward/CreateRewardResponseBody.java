package com.mauntung.mauntung.adapter.http.response.reward;

import com.mauntung.mauntung.application.port.reward.CreateRewardResponse;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Date;

@Getter
public class CreateRewardResponseBody {
    private final Data data;

    public CreateRewardResponseBody(CreateRewardResponse response) {
        data = new Data(response.getId(), response.getCreatedAt());
    }

    @Getter
    @RequiredArgsConstructor
    public static class Data {
        private final Long id;
        private final Date createdAt;
    }
}
