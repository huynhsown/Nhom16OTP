package vn.hcmute.enums;

import lombok.Getter;

@Getter
public enum RoleType {
    ADMIN(1L),
    CUSTOMER(2L);

    private final Long code;

    RoleType(Long code) {
        this.code = code;
    }

}
