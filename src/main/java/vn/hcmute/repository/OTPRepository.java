package vn.hcmute.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import vn.hcmute.entity.OTPEntity;
import vn.hcmute.entity.UserEntity;

import java.time.LocalDateTime;
import java.util.List;

public interface OTPRepository extends JpaRepository<OTPEntity, Long> {
    List<OTPEntity> findByExpiryTimeBefore(LocalDateTime now);
    OTPEntity findByExpiryTimeBeforeAndUserEntity(LocalDateTime expiryTime, UserEntity userEntity);
}
