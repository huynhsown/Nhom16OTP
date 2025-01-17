package vn.hcmute.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import vn.hcmute.entity.OTPEntity;
import vn.hcmute.entity.RoleEntity;
import vn.hcmute.entity.UserEntity;
import vn.hcmute.enums.RoleType;
import vn.hcmute.exception.DataNotFoundException;
import vn.hcmute.exception.PermissionDenyException;
import vn.hcmute.model.dto.UserDTO;
import vn.hcmute.repository.RoleRepository;
import vn.hcmute.repository.UserRepository;
import vn.hcmute.service.EmailService;
import vn.hcmute.service.OTPService;
import vn.hcmute.service.UserService;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private OTPService otpService;

    @Autowired
    private EmailService emailService;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public UserEntity createUser(UserDTO userDTO) throws PermissionDenyException {
        if (userRepository.existsByUserName(userDTO.getUserName())) {
            throw new DataIntegrityViolationException("Username already exists");
        }

        if (userRepository.existsByEmail(userDTO.getEmail())) {
            throw new DataIntegrityViolationException("Email already exists");
        }

        if (userRepository.existsByTelephone(userDTO.getTelephone())) {
            throw new DataIntegrityViolationException("Phone number already exists");
        }

        RoleEntity roleEntity = roleRepository.findByRoleCode(userDTO.getRoleId())
                .orElseThrow(() -> new DataNotFoundException("Role not found"));

        if (roleEntity.getRoleType().equals(RoleType.ADMIN)) {
            throw new PermissionDenyException("Can't regis an admin account");
        }

        UserEntity userEntity = UserEntity.builder()
                .userName(userDTO.getUserName())
                .firstName(userDTO.getFirstName())
                .lastName(userDTO.getLastName())
                .passWord(userDTO.getPassword())
                .telephone(userDTO.getTelephone())
                .email(userDTO.getEmail())
                .roleEntity(roleEntity)
                .isVerified(false)
                .build();

        String passwordEncoded = passwordEncoder.encode(userDTO.getPassword());
        userEntity.setPassWord(passwordEncoded);

        OTPEntity otpEntity = otpService.generateOTP(userEntity);
        emailService.sendOTPEmail(userDTO.getEmail(), otpEntity.getOtpCode());
        userEntity.setOtpEntity(otpEntity);
        return userRepository.save(userEntity);
    }

    @Override
    public boolean verifyOTP(UserDTO userDTO, String otp) {
        return false;
    }


}
