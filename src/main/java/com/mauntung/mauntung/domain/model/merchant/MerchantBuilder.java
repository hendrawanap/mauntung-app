package com.mauntung.mauntung.domain.model.merchant;

import com.mauntung.mauntung.domain.model.membership.Membership;

public interface MerchantBuilder {
    Merchant build() throws IllegalArgumentException;

    MerchantBuilder id(Long id);

    MerchantBuilder phone(String phone);

    MerchantBuilder profileImg(String profileImg);

    MerchantBuilder membership(Membership membership);
}
