package com.mauntung.mauntung.domain.model.merchant;

import java.util.Date;

public interface MerchantFactory {
    MerchantBuilder builder(String name, Date createdAt);
}
