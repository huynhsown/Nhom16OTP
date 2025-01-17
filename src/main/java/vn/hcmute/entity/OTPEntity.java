package vn.hcmute.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "otp")
@Getter
@Setter
public class OTPEntity extends BaseEntity{
    @Column(name = "otp_code", nullable = false)
    private String otpCode;

    @Column(name = "expiry_time", nullable = false)
    private LocalDateTime expiryTime;

    @OneToOne(optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private UserEntity userEntity;
}
