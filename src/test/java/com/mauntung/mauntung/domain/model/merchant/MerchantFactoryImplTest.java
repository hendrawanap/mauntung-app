package com.mauntung.mauntung.domain.model.merchant;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.Date;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

class MerchantFactoryImplTest {
    MerchantFactory merchantFactory = new MerchantFactoryImpl();
    static String name = "name";
    static Date createdAt = new Date();

    @BeforeEach
    void setUp() {
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void givenCompleteArgs_build_ShouldNotThrowException() {
        MerchantBuilder merchantBuilder = merchantFactory.builder(name, createdAt);
        assertDoesNotThrow(merchantBuilder::build);
    }

    @ParameterizedTest
    @MethodSource("malformedPhoneNumbersProvider")
    void givenMalformedPhone_build_shouldThrowException(String malformedPhone) {
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