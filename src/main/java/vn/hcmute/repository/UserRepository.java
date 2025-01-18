package vn.hcmute.repository;

import com.fasterxml.jackson.annotation.OptBoolean;
import jakarta.validation.constraints.NotBlank;
import org.springframework.data.jpa.repository.JpaRepository;
import vn.hcmute.entity.UserEntity;

import java.util.Optional;

public interface UserRepository extends JpaRepository<UserEntity, Long> {
    boolean existsByUserName(String userName);

    boolean existsByEmail(String email);

    boolean existsByTelephone(String telephone);

    Optional<UserEntity> findByEmail(String email);
}
