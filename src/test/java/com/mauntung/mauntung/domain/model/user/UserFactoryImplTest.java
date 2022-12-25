package com.mauntung.mauntung.domain.model.user;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.Date;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

class UserFactoryImplTest {
    UserFactory userFactory = new UserFactoryImpl();
    static String wellFormedEmail = "user@mail.com";
    static String password = "password";
    static Date createdAt = new Date();
    static Long id = 1L;
    static User.Role role = User.Role.MERCHANT;

    @BeforeEach
    void setUp() {
    }

    @AfterEach
    void tearDown() {
    }

    @ParameterizedTest
    @MethodSource("malformedEmailsProvider")
    void givenMalformedEmail_createWithoutId_shouldThrowException(String malformedEmail) {
        assertThrows(IllegalStateException.class, () -> userFactory.createWithoutId(malformedEmail, password, role, createdAt));
    }

    @ParameterizedTest
    @MethodSource("malformedEmailsProvider")
    void givenMalformedEmail_createWithId_shouldThrowException(String malformedEmail) {
        assertThrows(IllegalStateException.class, () -> userFactory.createWithId(malformedEmail, password, role, createdAt, id));
    }

    @Test
    void givenWellFormedEmail_createWithoutId_ShouldNotThrowException() {
        assertDoesNotThrow(() -> userFactory.createWithoutId(wellFormedEmail, password, role, createdAt));
    }

    @Test
    void givenWellFormedEmail_createWithId_ShouldNotThrowException() {
        assertDoesNotThrow(() -> userFactory.createWithId(wellFormedEmail, password, role, createdAt, id));
    }

    static Stream<String> malformedEmailsProvider() {
        return Stream.of(
            "user",
            "user@",
            "user@mail",
            "user.com"
        );
    }
}