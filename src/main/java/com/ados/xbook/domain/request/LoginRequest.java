package com.ados.xbook.domain.request;

import com.ados.xbook.exception.InvalidException;
import com.google.common.base.Strings;
import lombok.Data;

import static com.ados.xbook.util.Utils.validatePassword;
import static com.ados.xbook.util.Utils.validateUsername;

@Data
public class LoginRequest {
    private String username;
    private String password;

    public void validate() {

        if (Strings.isNullOrEmpty(username) || !validateUsername(username)) {
            throw new InvalidException("Username is invalid");
        }

        if (Strings.isNullOrEmpty(password) || !validatePassword(password)) {
            throw new InvalidException("Password is invalid");
        }

    }
    
}
