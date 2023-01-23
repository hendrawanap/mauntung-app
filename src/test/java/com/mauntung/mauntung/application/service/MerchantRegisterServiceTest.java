package com.mauntung.mauntung.application.service;

import com.mauntung.mauntung.application.exception.UserWithSpecifiedEmailExistedException;
import com.mauntung.mauntung.application.port.membership.MembershipRepository;
import com.mauntung.mauntung.application.port.merchant.MerchantRegisterCommand;
import com.mauntung.mauntung.application.port.merchant.MerchantRegisterUseCase;
import com.mauntung.mauntung.application.port.merchant.MerchantRepository;
import com.mauntung.mauntung.application.port.user.UserRepository;
import com.mauntung.mauntung.domain.model.merchant.Merchant;
import com.mauntung.mauntung.domain.model.user.User;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class MerchantRegisterServiceTest {
    private static UserRepository userRepository;
    private static MerchantRepository merchantRepository;
    private static MembershipRepository membershipRepository;
    private static User user;
    private static Merchant merchant;
    private static String email;
    private static String password;
    private static String merchantName;

    @BeforeAll
    static void beforeAll() {
        userRepository = mock(UserRepository.class);
        merchantRepository = mock(MerchantRepository.class);
        membershipRepository = mock(MembershipRepository.class);
        user = mock(User.class);
        merchant = mock(Merchant.class);
        email = "merchant1@mail.com";
        password = "password123";
        merchantName = "Laundree";
    }
    @Test
    void givenExistedEmail_apply_shouldThrowsException() {
        when(userRepository.findByEmail(email)).thenReturn(Optional.of(user));

        MerchantRepository merchantRepository = mock(MerchantRepository.class);

        MerchantRegisterCommand command = new MerchantRegisterCommand(email, password, merchantName);
        MerchantRegisterUseCase service = new MerchantRegisterService(userRepository, merchantRepository, membershipRepository);

        assertThrows(UserWithSpecifiedEmailExistedException.class, () -> service.apply(command));
    }

    @Test
    void givenNonExistedEmail_apply_shouldReturnSuccessResponse() {
        Long userId = 1L;
        Long merchantId = 1L;
        Long membershipId = 1L;

        when(merchant.getName()).thenReturn(merchantName);
        when(userRepository.findByEmail(email)).thenReturn(Optional.empty());
        when(userRepository.save(any())).thenReturn(Optional.of(userId));
        when(merchantRepository.save(any(), any())).thenReturn(Optional.of(merchantId));
        when(membershipRepository.save(any())).thenReturn(Optional.of(membershipId));

        MerchantRegisterUseCase service = new MerchantRegisterService(userRepository, merchantRepository, membershipRepository);
        MerchantRegisterCommand command = new MerchantRegisterCommand(email, password, merchantName);

        assertNotNull(service.apply(command));
    }
}