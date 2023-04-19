package com.lwdevelop.config;

import java.util.Arrays;
import java.util.List;

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
            if (admin == null) {
                admin = new Admin();
                List<String> roles = Arrays.asList(new String[] { "ADMIN" });
                admin.setUsername("admin");
                admin.setPassword("123456");
                admin.setEnabled(true);
                admin.setRoles(roles);
                admin.setRegIp(null);
                admin.setLastLoginIP(null);
                adminRepository.save(admin);
            }
           
        }catch (Exception e){
            throw e;
        }

    }
}