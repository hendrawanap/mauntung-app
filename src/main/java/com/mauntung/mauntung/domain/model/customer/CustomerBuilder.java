package com.mauntung.mauntung.domain.model.customer;

import com.mauntung.mauntung.domain.model.customer_membership.CustomerMembership;

import java.util.Date;
import java.util.Set;

public interface CustomerBuilder {
    Customer build() throws IllegalArgumentException;

    CustomerBuilder id(Long id);

    CustomerBuilder phone(String phone);

    CustomerBuilder birthDate(Date birthDate);

    CustomerBuilder gender(Customer.Gender gender);

    CustomerBuilder memberships(Set<CustomerMembership> customerMemberships);
}
