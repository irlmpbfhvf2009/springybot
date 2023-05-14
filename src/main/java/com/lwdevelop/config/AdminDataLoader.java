package com.lwdevelop.config;

import java.util.Arrays;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import com.lwdevelop.entity.Admin;
import com.lwdevelop.repository.AdminRepository;

@Component
public class AdminDataLoader implements CommandLineRunner {
    @Autowired
    private AdminRepository adminRepository;

    @Override
    public void run(String... args) throws Exception {
        try {
            Admin admin = adminRepository.findByUsername("admin");
            Admin test = adminRepository.findByUsername("test");
            if (admin == null) {
                admin = new Admin();
                admin.setUsername("admin");
                admin.setPassword("123456");
                admin.setEnabled(true);
                admin.setRoles(Arrays.asList(new String[] { "ADMIN" }));
                admin.setRegIp(null);
                admin.setLastLoginIP(null);
                adminRepository.save(admin);

                test = new Admin();
                test.setUsername("test");
                test.setPassword("123456");
                test.setEnabled(true);
                test.setRoles(Arrays.asList(new String[] { "ADMIN", "TEST" }));
                test.setRegIp(null);
                test.setLastLoginIP(null);
                adminRepository.save(admin);
            }
        } catch (Exception e) {
            throw e;
        }

    }
}