package com.mauntung.mauntung.domain.model.merchant;

import com.mauntung.mauntung.domain.model.membership.Membership;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Date;

@Getter
@AllArgsConstructor
public class Merchant {
    public static final String DUMMY_PROFILE_IMG_URL = "https://via.placeholder.com/120";

    private final Long id;
    private final String name;
    private final String phone;
    private final String profileImg;
    private final Date createdAt;
    private final Membership membership;

    public boolean hasCreatedMembership() {
        return membership != null;
    }
}
