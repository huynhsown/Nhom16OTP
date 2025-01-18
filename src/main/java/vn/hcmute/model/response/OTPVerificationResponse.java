package vn.hcmute.model.response;

import lombok.Data;

@Data
public class OTPVerificationResponse {
    private boolean success;
    private String errorMessage;
    private Object data;
}
