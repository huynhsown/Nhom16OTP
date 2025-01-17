package vn.hcmute.service;

import org.springframework.stereotype.Service;
import vn.hcmute.entity.UserEntity;
import vn.hcmute.exception.PermissionDenyException;
import vn.hcmute.model.dto.UserDTO;

@Service
public interface UserService {

    UserEntity createUser(UserDTO userDTO) throws PermissionDenyException;

}
