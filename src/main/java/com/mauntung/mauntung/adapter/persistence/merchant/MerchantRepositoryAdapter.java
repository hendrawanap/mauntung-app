package com.mauntung.mauntung.adapter.persistence.merchant;

import com.mauntung.mauntung.application.port.merchant.MerchantRepository;
import com.mauntung.mauntung.domain.model.merchant.Merchant;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class MerchantRepositoryAdapter implements MerchantRepository {
    private final JpaMerchantRepository jpaRepository;
    private final MerchantMapper mapper = new MerchantMapper();

    @Override
    public Optional<Merchant> findByUserId(Long userId) {
        Optional<MerchantEntity> entity = jpaRepository.findByUserId(userId);
        if (entity.isEmpty()) return Optional.empty();
        return Optional.of(mapper.entityToModel(entity.get()));
    }

    @Override
    public Optional<Long> save(Merchant merchant) {
        MerchantEntity entity = mapper.modelToEntity(merchant);
        try {
            entity = jpaRepository.save(entity);
        } catch (IllegalArgumentException ex) {
            return Optional.empty();
        }
        return Optional.of(entity.getId());
    }

    @Override
    public Optional<Long> save(Merchant merchant, Long userId) {
        MerchantEntity entity = mapper.modelToEntity(merchant, userId);
        try {
            entity = jpaRepository.save(entity);
        } catch (IllegalArgumentException ex) {
            return Optional.empty();
        }
        return Optional.of(entity.getId());
    }
}
