package com.ados.xbook.domain.request;

import com.ados.xbook.domain.entity.User;
import com.ados.xbook.exception.InvalidException;
import com.google.common.base.Strings;
import lombok.Data;

import static com.ados.xbook.util.Utils.*;

@Data
public class UserRequest {
    private String firstName;
    private String lastName;
    private String username;
    private String address;
    private String password;
    private Double amount;
    private String email;
    private String phone;
    private String role;

    public void validate(Boolean isUpdate) {

        if (Strings.isNullOrEmpty(firstName) || !validateFullName(firstName)) {
            throw new InvalidException("First name is invalid");
        }

        if (Strings.isNullOrEmpty(username) || !validateUsername(username)) {
            throw new InvalidException("Username is invalid");
        }

        if (Strings.isNullOrEmpty(address)) {
            throw new InvalidException("Address is invalid");
        }

        if (!isUpdate) {
            if (Strings.isNullOrEmpty(lastName) || !validateFullName(lastName)) {
                throw new InvalidException("Last name is invalid");
            }

            if (Strings.isNullOrEmpty(password) || !validatePassword(password)) {
                throw new InvalidException("Password is invalid");
            }
        }

        if (amount == null || amount < 0) {
            throw new InvalidException("Amount is invalid");
        }

        if (Strings.isNullOrEmpty(email) || !validateEmail(email)) {
            throw new InvalidException("Email is invalid");
        }

        if (Strings.isNullOrEmpty(phone) || !validatePhone(phone)) {
            throw new InvalidException("Phone is invalid");
        }

        if (Strings.isNullOrEmpty(role)) {
            throw new InvalidException("Role is invalid");
        }

    }

    public User create() {
        User user = new User();

        user.setFirstName(firstName);
        user.setLastName(lastName);
        user.setUsername(username);
        user.setAddress(address);
        user.setAmount(amount);
        user.setEmail(email);
        user.setPhone(phone);
        user.setRole(role);

        return user;
    }

    public User update(User user) {

        user.setFirstName(firstName);
        user.setLastName(lastName);
        user.setAddress(address);
        user.setAmount(amount);
        user.setEmail(email);
        user.setPhone(phone);
        user.setRole(role);

        return user;
    }

}
