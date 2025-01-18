package vn.hcmute.api;

import jakarta.validation.Valid;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import vn.hcmute.entity.UserEntity;
import vn.hcmute.model.dto.OTPRequestDTO;
import vn.hcmute.model.dto.UserDTO;
import vn.hcmute.service.OTPService;
import vn.hcmute.service.UserService;

import java.util.List;

@RestController
@RequestMapping("/v1/auth")
public class AuthAPI {

    @Autowired
    private UserService userService;

    @Autowired
    private OTPService otpService;

    @PostMapping("/register")
    public ResponseEntity<?> createUser(@Valid @RequestBody UserDTO userDTO, BindingResult result){
        try {
            if(result.hasErrors()){
                List<String> errorMessages = result.getFieldErrors()
                        .stream()
                        .map(DefaultMessageSourceResolvable::getDefaultMessage)
                        .toList();
                return ResponseEntity.badRequest().body(errorMessages);
            }
            if(!userDTO.getPassword().equals(userDTO.getConfirmPassword())){
                return ResponseEntity.badRequest().body("Password not match");
            }
            UserEntity user = userService.createUser(userDTO);
            return ResponseEntity.ok("Account created successfully");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @GetMapping("/ok")
    public ResponseEntity<?> hello(@RequestParam String email){
        try{
            return ResponseEntity.ok(userService.getOTP(email));
        }
        catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @PostMapping("/otp/verify")
    public ResponseEntity<?> verifyOTP(@RequestBody OTPRequestDTO otpRequestDTO, BindingResult result){
        try{
            if(result.hasErrors()){
                List<String> errorMessages = result.getFieldErrors()
                        .stream()
                        .map(DefaultMessageSourceResolvable::getDefaultMessage)
                        .toList();
                return ResponseEntity.badRequest().body(errorMessages);
            }
            boolean isVerify = userService.verifyOTP(otpRequestDTO);
            if(isVerify) {
                return ResponseEntity.ok("Xác thực thành công");
            }
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Xac thuc that bai");
        }catch (Exception ex){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
        }
    }

}
