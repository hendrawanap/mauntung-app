package com.mauntung.mauntung.domain.model.customer;

import com.mauntung.mauntung.domain.model.customer_membership.CustomerMembership;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Date;
import java.util.Set;
import java.util.UUID;

@Getter
@AllArgsConstructor
public class Customer {
    public static final String DUMMY_CUSTOMER_IMG = "https://via.placeholder.com/120";

    private final Long id;
    private final String name;
    private final String phone;
    private final UUID code;
    private final Date birthDate;
    private final Set<CustomerMembership> customerMemberships;
    private final Date createdAt;
}
