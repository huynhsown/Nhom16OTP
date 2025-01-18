package vn.hcmute.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import vn.hcmute.entity.OTPEntity;
import vn.hcmute.entity.UserEntity;
import vn.hcmute.enums.OTPStatus;

import java.time.LocalDateTime;
import java.util.List;

public interface OTPRepository extends JpaRepository<OTPEntity, Long> {
    List<OTPEntity> findByExpiryTimeBefore(LocalDateTime now);
    OTPEntity findByExpiryTimeAfterAndUserEntity(LocalDateTime expiryTime, UserEntity userEntity);
    List<OTPEntity> findByUserEntity(UserEntity userEntity);

    OTPEntity findByExpiryTimeAfterAndUserEntityAndStatus(LocalDateTime now, UserEntity userEntity, OTPStatus otpStatus);

    OTPEntity findByOtpCodeAndUserEntityAndStatus(String otpCode, UserEntity userEntity, OTPStatus otpStatus);

    List<OTPEntity> findByExpiryTimeBeforeOrStatus(LocalDateTime now, OTPStatus otpStatus);

    List<OTPEntity> findByUserEntityAndStatus(UserEntity userEntity, OTPStatus otpStatus);

    OTPEntity findFirstByUserEntityAndStatusOrderByCreatedDateDesc(UserEntity userEntity, OTPStatus otpStatus);
}
