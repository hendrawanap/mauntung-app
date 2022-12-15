package com.mauntung.mauntung.application.service;

import com.mauntung.mauntung.application.port.merchant.MerchantRegisterCommand;
import com.mauntung.mauntung.application.port.merchant.MerchantRegisterUseCase;
import com.mauntung.mauntung.application.port.merchant.MerchantRegisterResponse;
import com.mauntung.mauntung.application.port.merchant.MerchantRepository;
import com.mauntung.mauntung.application.port.user.UserRepository;
import com.mauntung.mauntung.domain.model.merchant.Merchant;
import com.mauntung.mauntung.domain.model.user.User;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class MerchantRegisterServiceTest {
    @Test
    void givenExistedEmail_apply_shouldReturnFailedResponse() {
        String email = "merchant1@mail.com";
        String password = "password123";
        String merchantName = "Laundree";

        User user = mock(User.class);

        UserRepository userRepository = mock(UserRepository.class);
        when(userRepository.findByEmail(email)).thenReturn(Optional.of(user));

        MerchantRepository merchantRepository = mock(MerchantRepository.class);

        // Testing start
        MerchantRegisterUseCase merchantRegisterService = new MerchantRegisterService(userRepository, merchantRepository);
        MerchantRegisterCommand command = new MerchantRegisterCommand(email, password, merchantName);
        MerchantRegisterResponse response = merchantRegisterService.apply(command);

        assertNotNull(response.getErrorResponse());
        assertNull(response.getSuccessResponse());
    }

    @Test
    void givenNonExistedEmail_apply_shouldReturnSuccessResponse() {
        String email = "merchant1@mail.com";
        String password = "password123";
        String merchantName = "Laundree";

        Long userId = 1L;

        Merchant merchant = mock(Merchant.class);
        Long merchantId = 1L;
        when(merchant.getName()).thenReturn(merchantName);

        UserRepository userRepository = mock(UserRepository.class);
        when(userRepository.findByEmail(email)).thenReturn(Optional.empty());
        when(userRepository.save(any())).thenReturn(Optional.of(userId));

        MerchantRepository merchantRepository = mock(MerchantRepository.class);
        when(merchantRepository.save(any())).thenReturn(Optional.of(merchantId));

        // Testing start
        MerchantRegisterUseCase merchantRegisterService = new MerchantRegisterService(userRepository, merchantRepository);
        MerchantRegisterCommand command = new MerchantRegisterCommand(email, password, merchantName);
        MerchantRegisterResponse response = merchantRegisterService.apply(command);

        assertNull(response.getErrorResponse());
        assertNotNull(response.getSuccessResponse());
    }
}