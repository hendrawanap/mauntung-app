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
    private final String imgUrl;
    private final Integer stock;
    private final Date startPeriod;
    private final Date endPeriod;

    public static Builder builder(String name, String description, String termsCondition, int cost) {
        return new Builder(name, description, termsCondition, cost);
    }

    @RequiredArgsConstructor
    public static class Builder {
        private final String name;
        private final String description;
        private final String termsCondition;
        private final int cost;
        private String imgUrl;
        private Integer stock;
        private Date startPeriod;
        private Date endPeriod;

        public Builder imgUrl(String imgUrl) {
            this.imgUrl = imgUrl;
            return this;
        }

        public Builder stock(Integer stock) {
            this.stock = stock;
            return this;
        }

        public Builder startPeriod(Date startPeriod) {
            this.startPeriod = startPeriod;
            return this;
        }

        public Builder endPeriod(Date endPeriod) {
            this.endPeriod = endPeriod;
            return this;
        }

        public CreateRewardCommand build() {
            return new CreateRewardCommand(name, description, termsCondition, cost, imgUrl, stock, startPeriod, endPeriod);
        }
    }
}
