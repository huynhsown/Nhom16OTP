package vn.hcmute.service;

import org.springframework.stereotype.Service;
import vn.hcmute.entity.OTPEntity;
import vn.hcmute.entity.UserEntity;

@Service
public interface OTPService {

    OTPEntity generateOTP(UserEntity userEntity);
    void deleteExpiredOTPs();
    String getOTP(UserEntity userEntity);
}
