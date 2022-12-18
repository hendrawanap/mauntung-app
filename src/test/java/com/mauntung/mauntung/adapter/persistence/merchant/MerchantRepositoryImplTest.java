package com.mauntung.mauntung.adapter.persistence.merchant;

import com.mauntung.mauntung.adapter.persistence.user.UserEntity;
import com.mauntung.mauntung.application.port.merchant.MerchantRepository;
import com.mauntung.mauntung.domain.model.merchant.Merchant;
import com.mauntung.mauntung.domain.model.merchant.MerchantFactory;
import com.mauntung.mauntung.domain.model.merchant.MerchantFactoryImpl;
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
class MerchantRepositoryImplTest {
    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private JpaMerchantRepository jpaMerchantRepository;

    @Test
    void findByUserId_shouldReturnMerchant() {
        String email = "merchant@mail.com";
        UserEntity userEntity = UserEntity.builder()
            .email(email)
            .role("merchant")
            .password("password123")
            .build();
        Long userId = entityManager.persistAndGetId(userEntity, Long.class);
        MerchantEntity merchantEntity = MerchantEntity.builder()
            .name("merchant")
            .user(UserEntity.builder().id(userId).build())
            .build();
        entityManager.persist(merchantEntity);
        entityManager.flush();

        MerchantRepository merchantRepository = new MerchantRepositoryImpl(jpaMerchantRepository);

        Optional<Merchant> merchant = merchantRepository.findByUserId(userId);
        assertTrue(merchant.isPresent());
    }

    @Test
    void findByUserId_shouldReturnEmpty() {
        String email = "merchant@mail.com";
        UserEntity userEntity = UserEntity.builder()
            .email(email)
            .role("merchant")
            .password("password123")
            .build();
        Long userId = entityManager.persistAndGetId(userEntity, Long.class);
        MerchantEntity merchantEntity = MerchantEntity.builder()
            .name("merchant")
            .user(UserEntity.builder().id(userId).build())
            .build();
        entityManager.persist(merchantEntity);
        entityManager.flush();

        MerchantRepository merchantRepository = new MerchantRepositoryImpl(jpaMerchantRepository);

        long differentUserId = userId != 100L ? 100L : 500L;
        Optional<Merchant> merchant = merchantRepository.findByUserId(differentUserId);
        assertTrue(merchant.isEmpty());
    }

    @Test
    void save_shouldReturnId() {
        String email = "merchant@mail.com";
        UserEntity userEntity = UserEntity.builder()
            .email(email)
            .role("merchant")
            .password("password123")
            .build();
        Long userId = entityManager.persistAndGetId(userEntity, Long.class);
        MerchantEntity merchantEntity = MerchantEntity.builder()
            .name("merchant")
            .user(UserEntity.builder().id(userId).build())
            .build();
        merchantEntity = entityManager.persist(merchantEntity);
        entityManager.flush();

        MerchantRepository merchantRepository = new MerchantRepositoryImpl(jpaMerchantRepository);
        MerchantFactory merchantFactory = new MerchantFactoryImpl();
        Merchant merchant = merchantFactory.builder("merchant2", merchantEntity.getCreatedAt())
            .id(merchantEntity.getId())
            .build();

        Optional<Long> merchantId = merchantRepository.save(merchant);
        assertTrue(merchantId.isPresent());
    }

    @Test
    void givenUserId_save_shouldReturnId() {
        String email = "merchant@mail.com";
        UserEntity userEntity = UserEntity.builder()
            .email(email)
            .role("merchant")
            .password("password123")
            .build();
        Long userId = entityManager.persistAndGetId(userEntity, Long.class);
        entityManager.flush();

        MerchantRepository merchantRepository = new MerchantRepositoryImpl(jpaMerchantRepository);
        MerchantFactory merchantFactory = new MerchantFactoryImpl();
        Merchant merchant = merchantFactory.builder("merchant", new Date()).build();

        Optional<Long> merchantId = merchantRepository.save(merchant, userId);
        assertTrue(merchantId.isPresent());
    }
}