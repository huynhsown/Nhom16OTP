package vn.hcmute.model.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import vn.hcmute.enums.OTPType;

@Data
public class ResendOTPRequest {
    @Email
    @NotNull
    private String email;

    @NotNull
    private OTPType otpType;
}
