package vn.hcmute.model.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OTPRequestDTO {
    @Email
    @NotEmpty
    private String email;

    @NotEmpty
    @Pattern(regexp = "^[0-9]{6}$", message = "OTP must be a 6-digit number.")
    private String OTP;
}
