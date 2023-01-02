package com.mauntung.mauntung.application.port.reward;

import com.mauntung.mauntung.domain.common.MessageBuilder;
import lombok.Getter;

@Getter
public class ListMembershipRewardsQuery {
    private final long merchantUserId;
    private final Integer minCost;
    private final Integer maxCost;
    private final String query;

    ListMembershipRewardsQuery(long merchantUserId, Integer minCost, Integer maxCost, String query) {
        this.merchantUserId = merchantUserId;
        this.minCost = minCost;
        this.maxCost = maxCost;
        this.query = query;
    }

    public static ListMembershipRewardsQueryBuilder builder(long merchantUserId) {
        return new ListMembershipRewardsQueryBuilder(merchantUserId);
    }

    public static class ListMembershipRewardsQueryBuilder {
        private final long merchantUserId;
        private Integer minCost;
        private Integer maxCost;
        private String query;

        ListMembershipRewardsQueryBuilder(long merchantUserId) {
            this.merchantUserId = merchantUserId;
        }

        public ListMembershipRewardsQuery build() {
            validate();
            return new ListMembershipRewardsQuery(merchantUserId, minCost, maxCost, query);
        }

        public ListMembershipRewardsQueryBuilder minCost(Integer minCost) {
            this.minCost = minCost;
            return this;
        }

        public ListMembershipRewardsQueryBuilder maxCost(Integer maxCost) {
            this.maxCost = maxCost;
            return this;
        }

        public ListMembershipRewardsQueryBuilder query(String query) {
            this.query = query;
            return this;
        }

        private void validate() throws IllegalArgumentException {
            MessageBuilder mb = new MessageBuilder();

            if (minCost != null && minCost < 0) mb.append("Min Cost must not be negative integer");
            if (maxCost != null && maxCost < 0) mb.append("Max Cost must not be negative integer");

            if (!mb.isEmpty()) throw new IllegalArgumentException(mb.toString());
        }
    }
}
