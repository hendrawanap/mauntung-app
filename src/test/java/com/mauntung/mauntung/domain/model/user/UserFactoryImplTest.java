package com.mauntung.mauntung.domain.model.user;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.Date;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

class UserFactoryImplTest {
    static UserFactory userFactory;
    static String wellFormedEmail;
    static String password;
    static Date createdAt;
    static Long id;
    static User.Role role;

    @BeforeAll
    static void beforeAll() {
        userFactory = new UserFactoryImpl();
        wellFormedEmail = "user@mail.com";
        password = "password";
        createdAt = new Date();
        id = 1L;
        role = User.Role.MERCHANT;
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
    void givenWellFormedEmail_createWithoutId_shouldNotThrowException() {
        assertDoesNotThrow(() -> userFactory.createWithoutId(wellFormedEmail, password, role, createdAt));
    }

    @Test
    void givenWellFormedEmail_createWithId_shouldNotThrowException() {
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