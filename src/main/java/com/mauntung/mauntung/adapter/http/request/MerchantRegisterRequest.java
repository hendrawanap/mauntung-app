package com.mauntung.mauntung.adapter.http.request;

import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@Data
public class MerchantRegisterRequest {
    @NotBlank(message = "merchantName can't be blank")
    private String merchantName;

    @Email
    @NotBlank(message = "email can't be blank")
    private String email;

    @NotBlank(message = "password can't be blank")
    private String password;
}
