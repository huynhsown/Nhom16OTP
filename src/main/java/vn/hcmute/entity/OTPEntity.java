package vn.hcmute.entity;

import jakarta.persistence.*;
import lombok.*;
import vn.hcmute.enums.OTPStatus;
import vn.hcmute.enums.OTPType;

import java.time.LocalDateTime;

@Entity
@Table(name = "otp")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OTPEntity extends BaseEntity{
    @Column(name = "otp_code", nullable = false)
    private String otpCode;

    @Column(name = "expiry_time", nullable = false)
    private LocalDateTime expiryTime;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private UserEntity userEntity;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private OTPStatus status = OTPStatus.ACTIVE;

    @Column(name = "otp_type")
    @Enumerated(EnumType.STRING)
    private OTPType otpType;
}
