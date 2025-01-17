package vn.hcmute.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import vn.hcmute.enums.RoleType;

import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "role")
@DynamicInsert
@DynamicUpdate
public class RoleEntity extends BaseEntity{

    @Column(name = "role_code", nullable = false, unique = true)
    private Long roleCode;

    @Enumerated(EnumType.STRING)
    @Column(name = "role_type", nullable = false, unique = true)
    private RoleType roleType;

    @OneToMany(mappedBy = "roleEntity", cascade = {CascadeType.PERSIST, CascadeType.MERGE}, orphanRemoval = true) // Đảm bảo mối quan hệ rõ ràng
    private List<UserEntity> userList;
}
