package com.example.ph35768_and103_assignment.init;

import android.text.TextUtils;
import android.util.Patterns;

public class InputValidator {
    public static boolean isValidEmail(String email) {
        return !TextUtils.isEmpty(email) && Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    public static boolean isValidPassword(String password) {
        return !TextUtils.isEmpty(password) && password.length() >= 6;
    }

    public static boolean isValidName(String name) {
        return !TextUtils.isEmpty(name) && name.matches("[a-zA-Z ]+");
    }

    public static boolean isValidPhone(String phone) {
        return !TextUtils.isEmpty(phone) && phone.startsWith("0") && phone.length() == 10;
    }

    public static boolean isValidAddress(String address) {
        return !TextUtils.isEmpty(address);
    }

    public static boolean isPasswordMatch(String password, String confirmPassword) {
        if (password == null || confirmPassword == null) {
            return false;
        }
        return password.equals(confirmPassword);
    }
}
