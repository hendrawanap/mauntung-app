package com.mauntung.mauntung.application.service;

import com.mauntung.mauntung.application.exception.UserWithSpecifiedEmailExistedException;
import com.mauntung.mauntung.application.port.membership.MembershipRepository;
import com.mauntung.mauntung.application.port.merchant.MerchantRegisterCommand;
import com.mauntung.mauntung.application.port.merchant.MerchantRegisterUseCase;
import com.mauntung.mauntung.application.port.merchant.MerchantRegisterResponse;
import com.mauntung.mauntung.application.port.merchant.MerchantRepository;
import com.mauntung.mauntung.application.port.user.UserRepository;
import com.mauntung.mauntung.domain.model.membership.*;
import com.mauntung.mauntung.domain.model.merchant.Merchant;
import com.mauntung.mauntung.domain.model.merchant.MerchantFactory;
import com.mauntung.mauntung.domain.model.merchant.MerchantFactoryImpl;
import com.mauntung.mauntung.domain.model.user.User;
import com.mauntung.mauntung.domain.model.user.UserFactory;
import com.mauntung.mauntung.domain.model.user.UserFactoryImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class MerchantRegisterService implements MerchantRegisterUseCase {
    private final UserRepository userRepository;
    private final MerchantRepository merchantRepository;
    private final MembershipRepository membershipRepository;
    private final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
    private final UserFactory userFactory = new UserFactoryImpl();
    private final MerchantFactory merchantFactory = new MerchantFactoryImpl();
    private final StampMembershipFactory stampMembershipFactory = new StampMembershipFactoryImpl();

    @Override
    public MerchantRegisterResponse apply(MerchantRegisterCommand command) {
        boolean userIsExist = userIsExist(command.getEmail());
        if (userIsExist) {
            throw new UserWithSpecifiedEmailExistedException();
        }

        User user = createUser(command.getEmail(), command.getPassword());
        Long userId = saveUserAndGetId(user);

        Merchant merchant = createMerchant(command.getMerchantName());
        Long merchantId = saveMerchantAndGetId(merchant, userId);

        Merchant savedMerchant = createMerchant(merchant, merchantId);
        Membership defaultMembership = createDefaultMembership(savedMerchant);
        saveDefaultMembership(defaultMembership);

        return buildResponse(userId, merchantId, merchant.getName());
    }

    private MerchantRegisterResponse buildResponse(long userId, long merchantId, String merchantName) {
        return new MerchantRegisterResponse(userId, merchantId, merchantName);
    }

    private void saveDefaultMembership(Membership membership) {
        membershipRepository.save(membership).orElseThrow(() -> new RuntimeException("Failed to save default membership"));
    }

    private Membership createDefaultMembership(Merchant merchant) {
        StampRules defaultRules = new StampRules(90, 90, 10);
        return stampMembershipFactory.builder("", merchant, Set.of(), new Date(), defaultRules, false).build();
    }

    private Long saveMerchantAndGetId(Merchant merchant, Long userId) {
        return merchantRepository.save(merchant, userId).orElseThrow(() -> new RuntimeException("Failed to save merchant"));
    }

    private Long saveUserAndGetId(User user) {
        return userRepository.save(user).orElseThrow(() -> new RuntimeException("Failed to save user"));
    }

    private boolean userIsExist(String email) {
        return userRepository.findByEmail(email).isPresent();
    }

    private User createUser(String email, String password) throws IllegalArgumentException {
        return userFactory.createWithoutId(email, passwordEncoder.encode(password), User.Role.MERCHANT, new Date());
    }

    private Merchant createMerchant(String merchantName) throws IllegalArgumentException {
        return merchantFactory.builder(merchantName, new Date()).build();
    }

    private Merchant createMerchant(Merchant merchant, long merchantId) {
        return merchantFactory.builder(merchant.getName(), merchant.getCreatedAt())
            .id(merchantId)
            .build();
    }
}
