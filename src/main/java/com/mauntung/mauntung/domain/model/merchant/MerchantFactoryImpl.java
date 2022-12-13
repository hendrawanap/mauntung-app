package com.mauntung.mauntung.domain.model.merchant;

import com.mauntung.mauntung.domain.common.MessageBuilder;
import com.mauntung.mauntung.domain.model.membership.Membership;
import lombok.RequiredArgsConstructor;

import java.util.Date;

public class MerchantFactoryImpl implements MerchantFactory {
    @Override
    public MerchantBuilder builder(String name, Date createdAt) {
        return new BuilderImpl(name, createdAt);
    }

    @RequiredArgsConstructor
    private static class BuilderImpl implements MerchantBuilder {
        private Long id;
        private final String name;
        private String phone;
        private String profileImg;
        private final Date createdAt;
        private Membership membership;

        @Override
        public Merchant build() throws IllegalArgumentException {
            validate();

            if (profileImg == null) profileImg = Merchant.DUMMY_PROFILE_IMG_URL;

            return new Merchant(id, name, phone, profileImg, createdAt, membership);
        }

        @Override
        public MerchantBuilder id(Long id) {
            this.id = id;
            return this;
        }

        @Override
        public MerchantBuilder phone(String phone) {
            this.phone = phone;
            return this;
        }

        @Override
        public MerchantBuilder profileImg(String profileImg) {
            this.profileImg = profileImg;
            return this;
        }

        @Override
        public MerchantBuilder membership(Membership membership) {
            this.membership = membership;
            return this;
        }

        private void validate() throws IllegalArgumentException {
            MessageBuilder mb = new MessageBuilder();

            if (phone != null && !phoneIsValid()) mb.append("Invalid Phone format (ex: +62878392012311)");

            if (mb.length() > 0) throw new IllegalArgumentException(mb.toString());
        }

        private boolean phoneIsValid() {
            return phone.matches("\\+\\d{10,13}");
        }
    }
}
