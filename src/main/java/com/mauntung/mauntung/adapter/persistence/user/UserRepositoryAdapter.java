package com.mauntung.mauntung.adapter.persistence.user;

import com.mauntung.mauntung.application.port.user.UserRepository;
import com.mauntung.mauntung.domain.model.user.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class UserRepositoryAdapter implements UserRepository {
    private final JpaUserRepository jpaRepository;
    private final UserMapper mapper = new UserMapper();

    @Override
    public Optional<User> findByEmail(String email) {
        Optional<UserEntity> entity = jpaRepository.findByEmail(email);
        if (entity.isEmpty()) return Optional.empty();
        return Optional.of(mapper.entityToModel(entity.get()));
    }

    @Override
    public Optional<Long> save(User user) {
        UserEntity entity = mapper.modelToEntity(user);
        entity = jpaRepository.save(entity);
        return Optional.of(entity.getId());
    }
}
