package com.ados.xbook.domain.request;

import lombok.Data;

import java.util.Date;

@Data
public class RegisterRequest {
    private String firstName;
    private String lastName;
    private String username;
    private String password;
    private String email;
    private String phone;
    private String address;
}
