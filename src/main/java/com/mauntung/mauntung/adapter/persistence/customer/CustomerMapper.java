package com.mauntung.mauntung.adapter.persistence.customer;

import com.mauntung.mauntung.adapter.persistence.user.UserEntity;
import com.mauntung.mauntung.domain.model.customer.Customer;
import com.mauntung.mauntung.domain.model.customer.CustomerFactory;
import com.mauntung.mauntung.domain.model.customer.CustomerFactoryImpl;

public class CustomerMapper {
    private final CustomerFactory customerFactory = new CustomerFactoryImpl();

    public Customer entityToModel(CustomerEntity entity) {
        Customer.Gender gender = null;
        if (entity.getGender() != null) {
            gender = entity.getGender() ? Customer.Gender.MALE : Customer.Gender.FEMALE;
        }
        return customerFactory.builder(
            entity.getName(),
            entity.getCustomerCode(),
            entity.getCreatedAt()
        )
        .id(entity.getId())
        .phone(entity.getPhone())
        .gender(gender)
        .birthDate(entity.getBirthDate())
        .build();
    }

    public CustomerEntity modelToEntity(Customer model) {
        Boolean gender = null;
        if (model.getGender() != null) {
            gender = model.getGender() == Customer.Gender.FEMALE;
        }
        return CustomerEntity.builder()
            .id(model.getId())
            .name(model.getName())
            .phone(model.getPhone())
            .customerCode(model.getCode())
            .gender(gender)
            .birthDate(model.getBirthDate())
            .build();
    }

    public CustomerEntity modelToEntity(Customer model, long userId) {
        Boolean gender = null;
        if (model.getGender() != null) {
            gender = model.getGender() == Customer.Gender.FEMALE;
        }
        return CustomerEntity.builder()
            .id(model.getId())
            .name(model.getName())
            .phone(model.getPhone())
            .customerCode(model.getCode())
            .gender(gender)
            .birthDate(model.getBirthDate())
            .user(UserEntity.builder().id(userId).build())
            .build();
    }
}
