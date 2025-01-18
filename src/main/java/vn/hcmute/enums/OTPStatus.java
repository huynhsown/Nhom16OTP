package vn.hcmute.enums;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum OTPStatus {
    ACTIVE("Active"),
    INACTIVE("Inactive"),
    VERIFIED("Verified");

    private final String displayName;
}
