package com.ados.xbook.util;

import com.google.common.base.Strings;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Utils {

    private static final String USERNAME_PATTERN = "^[A-Za-z0-9_]{4,30}$";

    private static final String FULLNAME_PATTERN =
            "^[a-zA-Z_ÀÁÂÃÈÉÊÌÍÒÓÔÕÙÚĂĐĨŨƠàáâãèéêìíòóôõùúăđĩũơƯĂẠẢẤẦẨẪẬẮẰẲẴẶ" +
                    "ẸẺẼỀỀỂưăạảấầẩẫậắằẳẵặẹẻẽềềểỄỆỈỊỌỎỐỒỔỖỘỚỜỞỠỢỤỦỨỪễệỉịọỏốồổỗộớờởỡợ" +
                    "ụủứừỬỮỰỲỴÝỶỸửữựỳỵỷỹ\\s]+$";

    private static final String PHONE_PATTERN = "(09|03|05|08|07)[0-9]{8}";

    private static final String EMAIL_PATTERN = "^[a-zA-Z0-9_!#$%&'*+/=?`{|}~^.-]+@[a-zA-Z0-9.-]+$";

    private static final String PASSWORD_PATTERN = "^(?=.*\\d)(?=.*[0-9])(?=.*[a-zA-Z])(?=.*[@$!%*#?&^_-]).{8,}$";

    static Pattern pattern;
    static Matcher matcher;

    public static Boolean validatePassword(String password) {
        if (Strings.isNullOrEmpty(password)) {
            return false;
        }
        pattern = Pattern.compile(PASSWORD_PATTERN);
        matcher = pattern.matcher(password);
        return matcher.find();
    }

    public static boolean validateEmail(String email) {
        if (Strings.isNullOrEmpty(email)) {
            return false;
        }
        pattern = Pattern.compile(EMAIL_PATTERN);
        matcher = pattern.matcher(email);
        return matcher.matches();
    }

    public static boolean validateFullName(String fullName) {
        if (Strings.isNullOrEmpty(fullName)) {
            return false;
        }
        pattern = Pattern.compile(FULLNAME_PATTERN);
        matcher = pattern.matcher(fullName);
        return matcher.matches();
    }

    public static boolean validatePhone(String phone) {
        if (Strings.isNullOrEmpty(phone)) {
            return false;
        }
        pattern = Pattern.compile(PHONE_PATTERN);
        matcher = pattern.matcher(phone);
        return matcher.matches();
    }

    public static boolean validateUsername(String username) {
        if (Strings.isNullOrEmpty(username)) {
            return false;
        }
        pattern = Pattern.compile(USERNAME_PATTERN);
        matcher = pattern.matcher(username);
        return matcher.matches();
    }

}
