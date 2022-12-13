package com.mauntung.mauntung.domain.model.customer;

import java.util.Date;
import java.util.UUID;

public interface CustomerFactory {
    CustomerBuilder builder(String name, UUID code, Date createdAt);
}
