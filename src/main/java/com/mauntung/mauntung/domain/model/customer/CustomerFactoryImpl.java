package com.mauntung.mauntung.domain.model.customer;

import com.mauntung.mauntung.domain.common.MessageBuilder;
import com.mauntung.mauntung.domain.model.customer_membership.CustomerMembership;
import lombok.RequiredArgsConstructor;

import java.util.Date;
import java.util.Set;
import java.util.UUID;

public class CustomerFactoryImpl implements CustomerFactory {
    @Override
    public CustomerBuilder builder(String name, UUID code, Date createdAt) {
        return new BuilderImpl(name, code, createdAt);
    }

    @RequiredArgsConstructor
    private static class BuilderImpl implements CustomerBuilder {
        private Long id;
        private final String name;
        private String phone;
        private final UUID code;
        private Date birthDate;
        private Set<CustomerMembership> customerMemberships;
        private final Date createdAt;

        @Override
        public Customer build() throws IllegalArgumentException {
            validate();
            return new Customer(id, name, phone, code, birthDate, customerMemberships, createdAt);
        }

        @Override
        public CustomerBuilder id(Long id) {
            this.id = id;
            return this;
        }

        @Override
        public CustomerBuilder phone(String phone) {
            this.phone = phone;
            return this;
        }

        @Override
        public CustomerBuilder birthDate(Date birthDate) {
            this.birthDate = birthDate;
            return this;
        }

        @Override
        public CustomerBuilder memberships(Set<CustomerMembership> customerMemberships) {
            this.customerMemberships = customerMemberships;
            return this;
        }

        private void validate() throws IllegalArgumentException {
            MessageBuilder mb = new MessageBuilder();

            if (phone != null && !phoneIsValid()) mb.append("Invalid Phone format (ex: +62878392012311)");

            if (mb.length() > 0) throw new IllegalArgumentException(mb.toString());
        }

        private boolean phoneIsValid() {
            return phone.matches("\\+\\d{10,13}");
        }
    }
}
