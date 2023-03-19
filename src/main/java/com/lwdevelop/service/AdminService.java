package com.lwdevelop.service;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;

import org.springframework.http.ResponseEntity;

import com.lwdevelop.dto.AdminDTO;
import com.lwdevelop.entity.Admin;
import com.lwdevelop.utils.ResponseUtils;

public interface AdminService {
    // DB CRUD
    Optional<Admin> findById(Long id);
    Admin findByUsername(String username);
    List<Admin> findAll(String username, int page, int pageSize);
    void saveAdmin(Admin admin);
    void deleteById(Long id);

    // Custom
    ResponseEntity<ResponseUtils.ResponseData> loginProcess(HttpServletRequest request, String username, String password);
    ResponseEntity<ResponseUtils.ResponseData> loginOutProcess(HttpServletRequest request, String token);
    ResponseEntity<ResponseUtils.ResponseData> getInfoProcess(HttpServletRequest request, String token);
    ResponseEntity<ResponseUtils.ResponseData> getAllAdminsProcess(HttpServletRequest request, int page,int pageSize, String input);
    ResponseEntity<ResponseUtils.ResponseData> updateAdminProcess(HttpServletRequest request, AdminDTO adminDTO);
    ResponseEntity<ResponseUtils.ResponseData> addAdminProcess(HttpServletRequest request, AdminDTO adminDTO);
    ResponseEntity<ResponseUtils.ResponseData> deleteAdminProcess(HttpServletRequest request, Map<String, String> requestData);
    ResponseEntity<ResponseUtils.ResponseData> passwordChangeProcess(HttpServletRequest request, AdminDTO adminDTO);
    


}