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
            User.Role.valueOf(entity.getRole().toUpperCase()),
            entity.getCreatedAt(),
            entity.getId()
        );
    }

    public UserEntity modelToEntity(User user) {
        return UserEntity.builder()
            .id(user.getId())
            .role(user.getRole().toString())
            .email(user.getEmail())
            .password(user.getPassword())
            .createdAt(user.getCreatedAt())
            .build();
    }
}
