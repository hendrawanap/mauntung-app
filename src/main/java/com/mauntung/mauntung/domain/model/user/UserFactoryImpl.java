package com.mauntung.mauntung.domain.model.user;

import com.mauntung.mauntung.domain.common.MessageBuilder;

import java.util.Date;

public class UserFactoryImpl implements UserFactory {

    @Override
    public User createWithId(String email, String password, Date createdAt, Long id) throws IllegalStateException {
        validate(email);
        return new User(id, email, password, createdAt);
    }

    @Override
    public User createWithoutId(String email, String password, Date createdAt) throws IllegalStateException {
        validate(email);
        return new User(null, email, password, createdAt);
    }

    private void validate(String email) throws IllegalStateException {
        MessageBuilder mb = new MessageBuilder();

        if (!validEmailFormat(email)) mb.append("Email is invalid");

        if (mb.length() > 0) throw new IllegalStateException(mb.toString());
    }

    private boolean validEmailFormat(String email) {
        return email.matches("^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$");
    }
}
