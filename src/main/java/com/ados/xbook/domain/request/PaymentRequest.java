package com.ados.xbook.domain.request;

import com.ados.xbook.exception.InvalidException;
import com.ados.xbook.util.Utils;
import com.google.common.base.Strings;
import lombok.Data;

@Data
public class PaymentRequest {
    private String firstName;
    private String lastName;
    private String phone;
    private String address;

    public void validate() {
        if (Strings.isNullOrEmpty(firstName) || Utils.validateFullName(firstName)) {
            throw new InvalidException("First name is invalid");
        }

        if (Strings.isNullOrEmpty(lastName) || Utils.validateFullName(lastName)) {
            throw new InvalidException("Last name is invalid");
        }

        if (Strings.isNullOrEmpty(phone) || Utils.validatePhone(phone)) {
            throw new InvalidException("Phone number is invalid");
        }

        if (Strings.isNullOrEmpty(address)) {
            throw new InvalidException("Address is invalid");
        }
    }
}
