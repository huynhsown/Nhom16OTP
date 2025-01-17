package vn.hcmute.repository;

import jakarta.validation.constraints.NotBlank;
import org.springframework.data.jpa.repository.JpaRepository;
import vn.hcmute.entity.UserEntity;

public interface UserRepository extends JpaRepository<UserEntity, Long> {
    boolean existsByUserName(String userName);

    boolean existsByEmail(String email);

    boolean existsByTelephone(String telephone);
}
