package com.mauntung.mauntung.domain.model.merchant;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.Date;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

class MerchantFactoryImplTest {
    static MerchantFactory merchantFactory;
    static String name;
    static Date createdAt;

    @BeforeAll
    static void beforeAll() {
        merchantFactory = new MerchantFactoryImpl();
        name = "name";
        createdAt = new Date();
    }

    @Test
    void givenCompleteArgs_build_shouldNotThrow() {
        MerchantBuilder merchantBuilder = merchantFactory.builder(name, createdAt);
        assertDoesNotThrow(merchantBuilder::build);
    }

    @ParameterizedTest
    @MethodSource("malformedPhoneNumbersProvider")
    void givenMalformedPhone_build_shouldThrow(String malformedPhone) {
        MerchantBuilder merchantBuilder = merchantFactory.builder(name, createdAt);
        merchantBuilder.phone(malformedPhone);
        assertThrows(IllegalArgumentException.class, merchantBuilder::build);
    }

    static Stream<String> malformedPhoneNumbersProvider() {
        return Stream.of(
            "087839481712",
            "+62",
            "+628729871",
            "+34567891010101"
        );
    }
}