package com.mauntung.mauntung.domain.model.membership;

public interface StampMembershipBuilder {
    StampMembership build();

    StampMembershipBuilder id(Long id);

    StampMembershipBuilder img(String img);
}
