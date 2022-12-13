package com.mauntung.mauntung.domain.model.user;

import lombok.*;

import java.util.Date;

@Getter
@ToString
@EqualsAndHashCode
@AllArgsConstructor(access = AccessLevel.PACKAGE)
public class User {
    private final Long id;
    private final String email;
    private final String password;
    private final Date createdAt;
}
