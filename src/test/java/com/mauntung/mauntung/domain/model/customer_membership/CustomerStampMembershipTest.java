package com.mauntung.mauntung.domain.model.customer_membership;

import com.mauntung.mauntung.domain.model.membership.StampMembership;
import com.mauntung.mauntung.domain.model.membership.StampRules;
import com.mauntung.mauntung.domain.model.merchant.Merchant;
import com.mauntung.mauntung.domain.model.redeem.Redeem;
import com.mauntung.mauntung.domain.model.stamp.Stamp;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.Date;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CustomerStampMembershipTest {
    static CustomerStampMembershipFactory membershipFactory;
    static Date now;
    static Set<Redeem> redeems;
    static Merchant merchant;

    @BeforeAll
    static void beforeAll() {
        membershipFactory = new CustomerStampMembershipFactoryImpl();
        now = new Date();
        redeems = Set.of();

        StampRules rules = mock(StampRules.class);
        when(rules.getCardCapacity()).thenReturn(10);

        StampMembership stampMembership = mock(StampMembership.class);
        when(stampMembership.getRules()).thenReturn(rules);

        merchant = mock(Merchant.class);
        when(merchant.getName()).thenReturn("Merchant");
        when(merchant.getMembership()).thenReturn(stampMembership);
    }

    static Stamp createUsableStamp() {
        Stamp usableStamp = mock(Stamp.class);
        when(usableStamp.isUsable()).thenReturn(true);
        when(usableStamp.getCreatedAt()).thenReturn(new Date());
        return usableStamp;
    }

    static Stamp createUsedStamp() {
        Stamp usedStamp = mock(Stamp.class);
        when(usedStamp.isUsable()).thenReturn(false);
        return usedStamp;
    }

    static Stamp createExpiredStamp() {
        Stamp expiredStamp = mock(Stamp.class);
        when(expiredStamp.isUsable()).thenReturn(false);
        return expiredStamp;
    }

    @Test
    void getBalance_shouldReturn5() {
        List<Stamp> stamps = List.of(
            createUsableStamp(),
            createUsableStamp(),
            createUsableStamp(),
            createUsableStamp(),
            createUsableStamp(),
            createUsedStamp(),
            createExpiredStamp()
        );

        CustomerStampMembership membership = membershipFactory.builder(merchant, now, redeems, stamps).build();
        assertEquals(5, membership.getBalance());
    }

    @Test
    void isFull_shouldReturnTrue() {
        List<Stamp> stamps = List.of(
            createUsableStamp(),
            createUsableStamp(),
            createUsableStamp(),
            createUsableStamp(),
            createUsableStamp(),
            createUsableStamp(),
            createUsableStamp(),
            createUsableStamp(),
            createUsableStamp(),
            createUsableStamp()
        );

        CustomerStampMembership membership = membershipFactory.builder(merchant, now, redeems, stamps).build();
        assertTrue(membership.isFull());
    }

    @Test
    void isFull_shouldReturnFalse() {
        List<Stamp> stamps = List.of(
            createUsableStamp(),
            createUsableStamp(),
            createUsableStamp(),
            createUsableStamp(),
            createUsableStamp(),
            createUsableStamp(),
            createUsableStamp(),
            createUsableStamp(),
            createUsedStamp(),
            createUsedStamp()
        );

        CustomerStampMembership membership = membershipFactory.builder(merchant, now, redeems, stamps).build();
        assertFalse(membership.isFull());
    }

    @Test
    void deductBalance_shouldReturnNull() {
        List<Stamp> stamps = List.of(
            createUsableStamp(),
            createUsableStamp(),
            createUsableStamp(),
            createUsableStamp(),
            createUsedStamp(),
            createUsedStamp()
        );

        CustomerStampMembership membership = membershipFactory.builder(merchant, now, redeems, stamps).build();
        assertNull(membership.deductBalance(10));
    }

    @Test
    void deductBalance_shouldReturn5Stamps() {
        List<Stamp> stamps = List.of(
            createUsableStamp(),
            createUsableStamp(),
            createUsableStamp(),
            createUsableStamp(),
            createUsableStamp(),
            createUsableStamp(),
            createUsedStamp(),
            createUsedStamp()
        );

        CustomerStampMembership membership = membershipFactory.builder(merchant, now, redeems, stamps).build();
        assertEquals(5, membership.deductBalance(5).size());
    }

}