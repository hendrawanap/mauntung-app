package com.mauntung.mauntung.application.service;

import com.mauntung.mauntung.application.port.merchant.MerchantRegisterCommand;
import com.mauntung.mauntung.application.port.merchant.MerchantRegisterUseCase;
import com.mauntung.mauntung.application.port.merchant.MerchantRegisterResponse;
import com.mauntung.mauntung.application.port.merchant.MerchantRepository;
import com.mauntung.mauntung.application.port.user.UserRepository;
import com.mauntung.mauntung.domain.model.merchant.Merchant;
import com.mauntung.mauntung.domain.model.merchant.MerchantFactory;
import com.mauntung.mauntung.domain.model.merchant.MerchantFactoryImpl;
import com.mauntung.mauntung.domain.model.user.User;
import com.mauntung.mauntung.domain.model.user.UserFactory;
import com.mauntung.mauntung.domain.model.user.UserFactoryImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class MerchantRegisterService implements MerchantRegisterUseCase {
    private final UserRepository userRepository;
    private final MerchantRepository merchantRepository;
    private final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
    private final UserFactory userFactory = new UserFactoryImpl();
    private final MerchantFactory merchantFactory = new MerchantFactoryImpl();

    @Override
    public MerchantRegisterResponse apply(MerchantRegisterCommand command) {
        MerchantRegisterResponse response = new MerchantRegisterResponse();

        boolean userIsExist = userIsExist(command.getEmail());
        if (userIsExist) {
            response.setErrorResponse("User with specified email is already exist");
            return response;
        }

        User newUser;
        try {
            newUser = createUser(command.getEmail(), command.getPassword());
        } catch (IllegalArgumentException ex) {
            response.setErrorResponse(ex.getMessage());
            return response;
        }

        Optional<Long> userId = userRepository.save(newUser);
        if (userId.isEmpty()) {
            response.setErrorResponse("Failed to create user");
            return response;
        }

        Merchant newMerchant;
        try {
            newMerchant = createMerchant(command.getMerchantName());
        } catch (IllegalArgumentException ex) {
            response.setErrorResponse(ex.getMessage());
            return response;
        }

        Optional<Long> merchantId = merchantRepository.save(newMerchant, userId.get());
        if (merchantId.isEmpty()) {
            response.setErrorResponse("Failed to create merchant");
            return response;
        }

        response.setSuccessResponse(
            MerchantRegisterResponse.SuccessResponse.builder()
                .merchantId(merchantId.get())
                .merchantName(newMerchant.getName())
                .userId(userId.get())
                .build()
        );

        return response;
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
}
