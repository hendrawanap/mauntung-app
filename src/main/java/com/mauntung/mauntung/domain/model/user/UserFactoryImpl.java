package com.mauntung.mauntung.domain.model.user;

import com.mauntung.mauntung.domain.common.MessageBuilder;

import java.util.Date;
import java.util.List;

public class UserFactoryImpl implements UserFactory {

    @Override
    public User createWithId(String email, String password, String role, Date createdAt, Long id) throws IllegalStateException {
        validate(email, role);
        return new User(id, email, password, role, createdAt);
    }

    @Override
    public User createWithoutId(String email, String password, String role, Date createdAt) throws IllegalStateException {
        validate(email, role);
        return new User(null, email, password, role, createdAt);
    }

    private void validate(String email, String role) throws IllegalStateException {
        MessageBuilder mb = new MessageBuilder();

        if (!validEmailFormat(email)) mb.append("Email is invalid");

        if (!validRole(role)) mb.append(String.format("Role is invalid (Valid: %s, %s)", User.Role.CUSTOMER, User.Role.MERCHANT));

        if (mb.length() > 0) throw new IllegalStateException(mb.toString());
    }

    private boolean validEmailFormat(String email) {
        return email.matches("^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$");
    }

    private boolean validRole(String role) {
        return List.of(User.Role.CUSTOMER, User.Role.MERCHANT).contains(role);
    }
}
