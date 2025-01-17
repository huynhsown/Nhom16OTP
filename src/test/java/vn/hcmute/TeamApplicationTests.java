package vn.hcmute;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import vn.hcmute.entity.RoleEntity;
import vn.hcmute.repository.RoleRepository;
import vn.hcmute.service.EmailService;

import java.util.List;

@SpringBootTest
class TeamApplicationTests {

	@Autowired
	RoleRepository repository;

	@Autowired
	EmailService emailService;

	@Test
	void contextLoads() {
		emailService.sendOTPEmail("sondanghuynh@gmail.com", "123456");
	}

}
