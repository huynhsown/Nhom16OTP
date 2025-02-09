package vn.hcmute.service;

import org.springframework.stereotype.Service;
import vn.hcmute.entity.OTPEntity;
import vn.hcmute.entity.UserEntity;
import vn.hcmute.enums.OTPType;
import vn.hcmute.exception.PermissionDenyException;
import vn.hcmute.model.dto.*;

@Service
public interface UserService {

    UserEntity createUser(UserDTO userDTO) throws PermissionDenyException;

    OTPDTO getOTP(String email);

    boolean verifyUser(OTPRequestDTO otpRequestDTO);

    boolean resetPassword(ResetPasswordDTO resetPasswordDTO);

    void isSend(String email, OTPType otpType);

    boolean loginAccount(LoginAccountDTO loginAccountDTO);
}
