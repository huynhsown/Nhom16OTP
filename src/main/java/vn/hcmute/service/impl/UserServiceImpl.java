package vn.hcmute.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vn.hcmute.entity.OTPEntity;
import vn.hcmute.entity.RoleEntity;
import vn.hcmute.entity.UserEntity;
import vn.hcmute.enums.OTPType;
import vn.hcmute.enums.RoleType;
import vn.hcmute.exception.DataNotFoundException;
import vn.hcmute.exception.PermissionDenyException;
import vn.hcmute.model.dto.OTPDTO;
import vn.hcmute.model.dto.OTPRequestDTO;
import vn.hcmute.model.dto.ResetPasswordDTO;
import vn.hcmute.model.dto.UserDTO;
import vn.hcmute.model.dto.LoginAccountDTO;
import vn.hcmute.repository.RoleRepository;
import vn.hcmute.repository.UserRepository;
import vn.hcmute.service.EmailService;
import vn.hcmute.service.OTPService;
import vn.hcmute.service.UserService;

import java.util.ArrayList;

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
        validateUserData(userDTO);
        RoleEntity roleEntity = getRoleEntity(userDTO.getRoleId());
        UserEntity userEntity = buildUserEntity(userDTO, roleEntity);
        encodePassword(userEntity, userDTO.getPassword());
        userRepository.save(userEntity);
        processOtpAndSendEmail(userEntity, userDTO.getEmail(), OTPType.EMAIL_VERIFICATION);
        return userRepository.save(userEntity);
    }

    private void validateUserData(UserDTO userDTO) {
        if (userRepository.existsByUserName(userDTO.getUserName())) {
            throw new DataIntegrityViolationException("Username already exists");
        }
        if (userRepository.existsByEmail(userDTO.getEmail())) {
            throw new DataIntegrityViolationException("Email already exists");
        }
        if (userRepository.existsByTelephone(userDTO.getTelephone())) {
            throw new DataIntegrityViolationException("Phone number already exists");
        }
    }

    private RoleEntity getRoleEntity(Long roleCode) throws PermissionDenyException {
        RoleEntity roleEntity = roleRepository.findByRoleCode(roleCode)
                .orElseThrow(() -> new DataNotFoundException("Không tìm thấy quyền"));

        if (roleEntity.getRoleType().equals(RoleType.ADMIN)) {
            throw new PermissionDenyException("Không thể đăng ký quyền admin");
        }

        return roleEntity;
    }

    private UserEntity buildUserEntity(UserDTO userDTO, RoleEntity roleEntity) {
        return UserEntity.builder()
                .userName(userDTO.getUserName())
                .firstName(userDTO.getFirstName())
                .lastName(userDTO.getLastName())
                .passWord(userDTO.getPassword())
                .telephone(userDTO.getTelephone())
                .email(userDTO.getEmail())
                .roleEntity(roleEntity)
                .isVerified(false)
                .otpEntity(new ArrayList<>())
                .build();
    }

    private void encodePassword(UserEntity userEntity, String rawPassword) {
        String passwordEncoded = passwordEncoder.encode(rawPassword);
        userEntity.setPassWord(passwordEncoded);
    }

    private void processOtpAndSendEmail(UserEntity userEntity, String email, OTPType otpType) {
        OTPEntity otpEntity = otpService.generateOTP(userEntity, otpType);
        emailService.sendOTPEmail(email, otpEntity.getOtpCode());
        userEntity.getOtpEntity().add(otpEntity);
    }

    @Override
    public OTPDTO getOTP(String email) {
        UserEntity userEntity = userRepository.findByEmail(email)
                .orElseThrow(() -> new DataNotFoundException("Account does not exist"));
        return otpService.getOTP(userEntity);
    }

    @Override
    public boolean verifyUser(OTPRequestDTO otpRequestDTO) {
        UserEntity userEntity = userRepository.findByEmail(otpRequestDTO.getEmail())
                .orElseThrow(() -> new DataNotFoundException("Account does not exist"));
        if(otpService.verifyOTP(otpRequestDTO.getOTP(), userEntity, OTPType.EMAIL_VERIFICATION)){
            userEntity.setVerified(true);
            userRepository.save(userEntity);
            return true;
        }
        return false;
    }

    @Override
    @Transactional
    public boolean resetPassword(ResetPasswordDTO resetPasswordDTO) {
        UserEntity userEntity = userRepository.findByEmail(resetPasswordDTO.getEmail())
                .orElseThrow(() -> new DataNotFoundException("Account does not exist"));

        if (!otpService.verifyOTP(resetPasswordDTO.getOtp(), userEntity, OTPType.PASSWORD_RESET)) {
            return false;
        }

        String encodedPassword = passwordEncoder.encode(resetPasswordDTO.getNewPassword());
        userEntity.setPassWord(encodedPassword);
        userRepository.save(userEntity);
        return true;
    }

    @Override
    public void isSend(String email, OTPType otpType) {
        UserEntity userEntity = userRepository.findByEmail(email)
                .orElseThrow(() -> new DataNotFoundException("Account does not exist"));
        processOtpAndSendEmail(userEntity, email, otpType);
    }

    @Override
    public boolean loginAccount(LoginAccountDTO loginAccountDTO){
        UserEntity userEntity = userRepository.findByEmail(loginAccountDTO.getEmail())
                .orElseThrow(() -> new DataNotFoundException("Account does not exist"));

        if (!otpService.verifyOTP(loginAccountDTO.getOtp(), userEntity, OTPType.LOGIN)) {
            return false;
        }

        if (!passwordEncoder.matches(loginAccountDTO.getPassword(), userEntity.getPassWord())) {
            return false;
        }
        return true;
    }
}
