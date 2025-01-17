package vn.hcmute.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import vn.hcmute.entity.RoleEntity;
import vn.hcmute.enums.RoleType;
import vn.hcmute.repository.RoleRepository;

@Component
public class DataInitializer implements CommandLineRunner {

    @Autowired
    private RoleRepository roleRepository;

    @Override
    public void run(String... args) {
        if (roleRepository.count() == 0) {
            RoleEntity adminRole = new RoleEntity();
            adminRole.setRoleType(RoleType.ADMIN);
            adminRole.setRoleCode(RoleType.ADMIN.getCode());
            roleRepository.save(adminRole);

            RoleEntity customerRole = new RoleEntity();
            customerRole.setRoleType(RoleType.CUSTOMER);
            customerRole.setRoleCode(RoleType.CUSTOMER.getCode());
            roleRepository.save(customerRole);
        }
    }
}
