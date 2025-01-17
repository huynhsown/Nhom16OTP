package vn.hcmute.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import vn.hcmute.repository.OTPRepository;

import java.time.LocalDateTime;
import java.util.Random;

@Service
public class OTPService {
    private static final int OTP_LENGTH = 6;
    public String generateOTP() {
        Random random = new Random();
        StringBuilder otp = new StringBuilder();

        for (int i = 0; i < OTP_LENGTH; i++) {
            otp.append(random.nextInt(10));
        }

        return otp.toString();
    }
}
