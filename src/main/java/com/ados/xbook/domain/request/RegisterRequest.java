package com.ados.xbook.domain.request;

import com.ados.xbook.exception.InvalidException;
import com.google.common.base.Strings;
import lombok.Data;

import static com.ados.xbook.util.Utils.*;

@Data
public class RegisterRequest {
    private String firstName;
    private String lastName;
    private String username;
    private String password;
    private String email;
    private String phone;
    private String address;

    public void validate() {

        if (Strings.isNullOrEmpty(firstName) || !validateFullName(firstName)) {
            throw new InvalidException("First name is invalid");
        }

        if (Strings.isNullOrEmpty(lastName) || !validateFullName(lastName)) {
            throw new InvalidException("Last name is invalid");
        }

        if (Strings.isNullOrEmpty(username) || !validateUsername(username)) {
            throw new InvalidException("Username is invalid");
        }

        if (Strings.isNullOrEmpty(address)) {
            throw new InvalidException("Address is invalid");
        }

        if (Strings.isNullOrEmpty(password) || !validatePassword(password)) {
            throw new InvalidException("Password is invalid");
        }

        if (Strings.isNullOrEmpty(email) || !validateEmail(email)) {
            throw new InvalidException("Email is invalid");
        }

        if (Strings.isNullOrEmpty(phone) || !validatePhone(phone)) {
            throw new InvalidException("Phone is invalid");
        }
        
    }
}
