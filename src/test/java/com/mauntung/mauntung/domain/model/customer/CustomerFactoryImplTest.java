package com.mauntung.mauntung.domain.model.customer;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.Date;
import java.util.UUID;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

class CustomerFactoryImplTest {
    CustomerFactory customerFactory = new CustomerFactoryImpl();
    static String name = "name";
    static UUID code = UUID.randomUUID();
    static Date createdAt = new Date();

    static Stream<String> malformedPhoneNumbersProvider() {
        return Stream.of(
            "087839481712",
            "+62",
            "+628729871",
            "+34567891010101"
        );
    }

    @Test
    void givenCompleteArgs_build_shouldNotThrow() {
        CustomerBuilder customerBuilder = customerFactory.builder(name, code, createdAt);
        assertDoesNotThrow(customerBuilder::build);
    }

    @ParameterizedTest
    @MethodSource("malformedPhoneNumbersProvider")
    void givenMalformedPhone_build_shouldThrow(String malformedPhone) {
        CustomerBuilder customerBuilder = customerFactory.builder(name, code, createdAt);
        customerBuilder.phone(malformedPhone);
        assertThrows(IllegalArgumentException.class, customerBuilder::build);
    }
}