package com.mauntung.mauntung.adapter.persistence.user;

import com.mauntung.mauntung.application.port.user.UserRepository;
import com.mauntung.mauntung.domain.model.user.User;
import com.mauntung.mauntung.domain.model.user.UserFactory;
import com.mauntung.mauntung.domain.model.user.UserFactoryImpl;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.util.Date;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class UserRepositoryImplTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private JpaUserRepository jpaUserRepository;

    @Test
    void findByEmail_shouldReturnUser() {
        String email = "merchant@mail.com";
        UserEntity entity = UserEntity.builder()
            .email(email)
            .role("merchant")
            .password("password123")
            .build();
        entityManager.persist(entity);
        entityManager.flush();

        UserRepository userRepository = new UserRepositoryImpl(jpaUserRepository);

        Optional<User> user = userRepository.findByEmail(email);
        assertTrue(user.isPresent());
    }

    @Test
    void findByEmail_shouldReturnEmpty() {
        String email = "merchant@mail.com";
        UserEntity entity = UserEntity.builder()
            .email(email)
            .role("merchant")
            .password("password123")
            .build();
        entityManager.persist(entity);
        entityManager.flush();

        UserRepository userRepository = new UserRepositoryImpl(jpaUserRepository);

        Optional<User> user = userRepository.findByEmail("invalid@mail.com");
        assertTrue(user.isEmpty());
    }

    @Test
    void save_shouldReturnId() {
        UserFactory userFactory = new UserFactoryImpl();
        User user = userFactory.createWithoutId("merchant@mail.com", "password", "merchant", new Date());
        UserRepository userRepository = new UserRepositoryImpl(jpaUserRepository);

        Optional<Long> userId = userRepository.save(user);
        assertTrue(userId.isPresent());
    }
}