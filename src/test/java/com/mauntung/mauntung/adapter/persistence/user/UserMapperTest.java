package com.mauntung.mauntung.adapter.persistence.user;

import com.mauntung.mauntung.domain.model.user.User;
import org.junit.jupiter.api.Test;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

class UserMapperTest {
    @Test
    void entityToModel_shouldNotThrowsException() {
        UserMapper userMapper = new UserMapper();
        UserEntity entity = UserEntity.builder()
            .id(1L)
            .role("merchant")
            .email("merchant@mail.com")
            .password("password123")
            .createdAt(new Date())
            .build();
        assertDoesNotThrow(() -> userMapper.entityToModel(entity));
        assertEquals(userMapper.entityToModel(entity).getRole(), User.Role.MERCHANT);
    }
}