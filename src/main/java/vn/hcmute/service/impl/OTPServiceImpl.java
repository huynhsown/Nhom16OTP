package vn.hcmute.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vn.hcmute.converter.OTPDTOConverter;
import vn.hcmute.entity.OTPEntity;
import vn.hcmute.entity.UserEntity;
import vn.hcmute.exception.OTPExpiredException;
import vn.hcmute.model.dto.OTPDTO;
import vn.hcmute.repository.OTPRepository;
import vn.hcmute.service.OTPService;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Random;

@Service
public class OTPServiceImpl implements OTPService {
    @Autowired
    private OTPRepository otpRepository;

    @Autowired
    private OTPDTOConverter otpdtoConverter;

    private static final int OTP_LENGTH = 6;

    @Override
    public OTPEntity generateOTP(UserEntity userEntity) {
        Random random = new Random();
        StringBuilder otp = new StringBuilder();

        for (int i = 0; i < OTP_LENGTH; i++) {
            otp.append(random.nextInt(9) + 1);
        }

        OTPEntity otpEntity = new OTPEntity();
        otpEntity.setOtpCode(otp.toString());

        otpEntity.setExpiryTime(LocalDateTime.now().plusSeconds(20));

        otpEntity.setUserEntity(userEntity);

        return otpEntity;
    }

    @Override
    @Scheduled(cron = "0 * * * * *")
    @Transactional
    public void deleteExpiredOTPs() {
        LocalDateTime now = LocalDateTime.now();
        List<OTPEntity> expiredOTPs = otpRepository.findByExpiryTimeBefore(now);

        if (!expiredOTPs.isEmpty()) {
            otpRepository.deleteAllInBatch(expiredOTPs);
            otpRepository.flush();
            System.out.println("Expired OTPs deleted: " + expiredOTPs.size());
        } else {
            System.out.println("No expired OTPs found at this time.");
        }
    }

    @Override
    public OTPDTO getOTP(UserEntity userEntity) {
        LocalDateTime now = LocalDateTime.now();
        OTPEntity otpEntity = otpRepository.findByExpiryTimeAfterAndUserEntity(now, userEntity);

        if (otpEntity == null || otpEntity.getExpiryTime().isBefore(now)) {
            throw new OTPExpiredException("OTP code has expired");
        }

        return otpdtoConverter.toOTPDTO(otpEntity);
    }
}
