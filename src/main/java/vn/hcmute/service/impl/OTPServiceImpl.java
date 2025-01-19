package vn.hcmute.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vn.hcmute.converter.OTPDTOConverter;
import vn.hcmute.entity.OTPEntity;
import vn.hcmute.entity.UserEntity;
import vn.hcmute.enums.OTPStatus;
import vn.hcmute.enums.OTPType;
import vn.hcmute.exception.OTPException;
import vn.hcmute.model.dto.OTPDTO;
import vn.hcmute.repository.OTPRepository;
import vn.hcmute.service.OTPService;

import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class OTPServiceImpl implements OTPService {

    @Autowired
    private OTPRepository otpRepository;

    @Autowired
    private OTPDTOConverter otpdtoConverter;

    private final SecureRandom secureRandom = new SecureRandom();

    @Override
    @Transactional
    public OTPEntity generateOTP(UserEntity userEntity, OTPType otpType) {
        validateUserEntity(userEntity);
        deactivateExistingOTPs(userEntity);

        String otpCode = generateSecureOTP();
        OTPEntity otpEntity = createOTPEntity(userEntity, otpCode, otpType);

        return otpRepository.save(otpEntity);
    }

    @Override
    @Transactional
    public OTPDTO getOTP(UserEntity userEntity) {
        validateUserEntity(userEntity);

        LocalDateTime now = LocalDateTime.now();
        OTPEntity otpEntity = otpRepository.findByExpiryTimeAfterAndUserEntityAndStatus(
                now, userEntity, OTPStatus.ACTIVE);

        if (otpEntity == null) {
            return null;
        }

        return otpdtoConverter.toOTPDTO(otpEntity);
    }

    @Override
    @Transactional
    public boolean verifyOTP(String otpCode, UserEntity userEntity, OTPType otpType) {
        validateUserEntity(userEntity);

        OTPEntity otpEntity = otpRepository.findFirstByUserEntityAndStatusOrderByCreatedDateDesc(
                userEntity, OTPStatus.ACTIVE);

        if (otpEntity == null) {
            return false;
        }

        if(!otpEntity.getOtpType().equals(otpType)){
            return false;
        }

        if (LocalDateTime.now().isAfter(otpEntity.getExpiryTime())) {
            deactivateOTP(otpEntity);
            throw new OTPException("OTP has expired. Please request a new one.");
        }

        if (otpCode.equals(otpEntity.getOtpCode())) {
            verifiedOTP(otpEntity);
            return true;
        }

        otpRepository.save(otpEntity);
        return false;
    }

    @Scheduled(cron = "0 */5 * * * *")
    @Transactional
    public void deleteExpiredOTPs() {
        LocalDateTime now = LocalDateTime.now();
        List<OTPEntity> expiredOTPs = otpRepository.findByExpiryTimeBefore(
                now);

        System.out.println("Size cua OTPS:" + expiredOTPs.size());

        if (!expiredOTPs.isEmpty()) {
            otpRepository.deleteAllInBatch(expiredOTPs);
            otpRepository.flush();
        }
    }

    private void validateUserEntity(UserEntity userEntity) {
        if (userEntity == null || userEntity.getId() == null) {
            throw new IllegalArgumentException("Invalid user entity");
        }
    }

    private String generateSecureOTP() {
        StringBuilder otp = new StringBuilder();
        for (int i = 0; i < 6; i++) {
            otp.append(secureRandom.nextInt(10));
        }
        return otp.toString();
    }

    private OTPEntity createOTPEntity(UserEntity userEntity, String otpCode, OTPType otpType) {
        return OTPEntity.builder()
                .otpType(otpType)
                .otpCode(otpCode)
                .expiryTime(LocalDateTime.now().plusMinutes(5))
                .userEntity(userEntity)
                .status(OTPStatus.ACTIVE)
                .build();
    }

    @Transactional
    private void deactivateExistingOTPs(UserEntity userEntity) {
        List<OTPEntity> existingOTPs = otpRepository.findByUserEntityAndStatus(
                userEntity, OTPStatus.ACTIVE);

        existingOTPs.forEach(otp -> {
            if(!otp.getUserEntity().isVerified()) {
                otp.setStatus(OTPStatus.INACTIVE);
            }
        });
        otpRepository.saveAll(existingOTPs);
    }

    private void verifiedOTP(OTPEntity otpEntity) {
        otpEntity.setStatus(OTPStatus.VERIFIED);
        otpRepository.save(otpEntity);
    }

    private void deactivateOTP(OTPEntity otpEntity) {
        otpEntity.setStatus(OTPStatus.INACTIVE);
        otpRepository.save(otpEntity);
    }
}