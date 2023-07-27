package org.springybot.service;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;

import org.springframework.http.ResponseEntity;

import org.springybot.dto.AdminDTO;
import org.springybot.entity.Admin;
import org.springybot.utils.ResponseUtils;

public interface AdminService {
    // DB CRUD
    Optional<Admin> findById(Long id);

    Admin findByUsername(String username);

    List<Admin> findAll(String username, int page, int pageSize);

    void saveAdmin(Admin admin);

    void deleteById(Long id);

    // Custom
    ResponseEntity<ResponseUtils.ResponseData> loginProcess(HttpServletRequest request, String username,
            String password);

    ResponseEntity<ResponseUtils.ResponseData> loginOutProcess(String token);

    ResponseEntity<ResponseUtils.ResponseData> getInfoProcess(String token);

    ResponseEntity<ResponseUtils.ResponseData> getAllAdminsProcess(int page, int pageSize, String input);

    ResponseEntity<ResponseUtils.ResponseData> updateAdminProcess(AdminDTO adminDTO);

    ResponseEntity<ResponseUtils.ResponseData> addAdminProcess(HttpServletRequest request, AdminDTO adminDTO);

    ResponseEntity<ResponseUtils.ResponseData> deleteAdminProcess(Map<String, String> requestData);

    ResponseEntity<ResponseUtils.ResponseData> passwordChangeProcess(AdminDTO adminDTO);

}