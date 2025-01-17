package vn.hcmute.entity;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Entity
@Table(name = "user")
@Getter
@Setter
@Builder
public class UserEntity extends BaseEntity{
    @Column(name = "username", unique = true)
    private String userName;

    @Column(name = "password", columnDefinition = "TEXT")
    @Lob
    private String passWord;

    @Column(name = "telephone", unique = true)
    private String telephone;

    @Column(name = "firstname")
    private String firstName;

    @Column(name = "lastname")
    private String lastName;

    @Column(name = "email", unique = true)
    private String email;

    @Column(name = "is_verified")
    private boolean isVerified;

    @ManyToOne
    @JoinColumn(name = "role_id", nullable = false)
    private RoleEntity roleEntity;

    @OneToMany(mappedBy = "userEntity", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OTPEntity> otpEntities;
}
