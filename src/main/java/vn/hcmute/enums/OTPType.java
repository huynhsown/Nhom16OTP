package vn.hcmute.enums;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum OTPType {
    EMAIL_VERIFICATION("Email Verification"),
    PASSWORD_RESET("Password Reset"),
    LOGIN("Login");

    private final String displayName;
}
