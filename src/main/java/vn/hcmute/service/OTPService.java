package vn.hcmute.service;

import org.springframework.stereotype.Service;
import vn.hcmute.entity.OTPEntity;
import vn.hcmute.entity.UserEntity;
import vn.hcmute.model.dto.OTPDTO;

@Service
public interface OTPService {

    OTPEntity generateOTP(UserEntity userEntity);
    void deleteExpiredOTPs();
    OTPDTO getOTP(UserEntity userEntity);
}
