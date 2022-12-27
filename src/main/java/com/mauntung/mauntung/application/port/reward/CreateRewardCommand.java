package com.mauntung.mauntung.application.port.reward;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Date;

@Getter
@AllArgsConstructor
public class CreateRewardCommand {
    private final String name;
    private final String description;
    private final String termsCondition;
    private final int cost;
    private final Integer stock;
    private final Date startPeriod;
    private final Date endPeriod;
    private final long userId;

    public static Builder builder(String name, String description, String termsCondition, int cost, long userId) {
        return new Builder(name, description, termsCondition, cost, userId);
    }

    @RequiredArgsConstructor
    public static class Builder {
        private final String name;
        private final String description;
        private final String termsCondition;
        private final int cost;
        private Integer stock;
        private Date startPeriod;
        private Date endPeriod;
        private final long userId;

        public Builder stock(Integer stock) {
            this.stock = stock;
            return this;
        }

        public Builder periods(Date startPeriod, Date endPeriod) {
            this.startPeriod = startPeriod;
            this.endPeriod = endPeriod;
            return this;
        }

        public CreateRewardCommand build() {
            return new CreateRewardCommand(name, description, termsCondition, cost, stock, startPeriod, endPeriod, userId);
        }
    }
}
