package com.mauntung.mauntung.adapter.persistence.user;

import com.mauntung.mauntung.domain.model.user.User;
import com.mauntung.mauntung.domain.model.user.UserFactory;
import com.mauntung.mauntung.domain.model.user.UserFactoryImpl;

public class UserMapper {
    private final UserFactory userFactory = new UserFactoryImpl();

    public User entityToModel(UserEntity entity) {
        return userFactory.createWithId(
            entity.getEmail(),
            entity.getPassword(),
            entity.getRole(),
            entity.getCreatedAt(),
            entity.getId()
        );
    }

    public UserEntity modelToEntity(User user) {
        return UserEntity.builder()
            .id(user.getId())
            .role(user.getRole())
            .email(user.getEmail())
            .password(user.getPassword())
            .createdAt(user.getCreatedAt())
            .build();
    }
}
