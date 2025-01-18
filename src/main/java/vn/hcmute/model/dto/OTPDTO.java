package vn.hcmute.model.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class OTPDTO extends BaseDTO{
    private String otpCode;
    private LocalDateTime expiryTime;
}
