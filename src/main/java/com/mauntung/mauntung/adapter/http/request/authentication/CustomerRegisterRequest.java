package com.mauntung.mauntung.adapter.http.request.authentication;

import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@Data
public class CustomerRegisterRequest {
    @NotBlank(message = "fullName can't be blank")
    private String fullName;

    @Email
    @NotBlank(message = "email can't be blank")
    private String email;

    @NotBlank(message = "password can't be blank")
    private String password;
}
