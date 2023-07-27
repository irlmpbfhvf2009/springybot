package org.springybot.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springybot.dto.AdminDTO;
import org.springybot.entity.Admin;
import org.springybot.repository.AdminRepository;
import org.springybot.service.AdminService;
import org.springybot.utils.CommUtils;
import org.springybot.utils.JwtUtils;
import org.springybot.utils.ResponseUtils;
import org.springybot.utils.RetEnum;
import org.springybot.utils.ResponseUtils.ResponseData;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class AdminServiceImpl implements AdminService, UserDetailsService {

    @Autowired
    private AdminRepository adminRepository;

    @Override
    public void saveAdmin(Admin admin) {
        adminRepository.save(admin);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Admin admin = adminRepository.findByUsername(username);
        if (admin == null) {
            throw new UsernameNotFoundException("User not found");
        }
        UserDetails userDetails = User.builder()
                .username(admin.getUsername())
                .password("{noop}" + admin.getPassword())
                .roles(admin.getRoles().get(0))
                .authorities(new SimpleGrantedAuthority(admin.getRoles().get(0)))
                .build();
        return userDetails;
    }

    @Override
    public Admin findByUsername(String username) {
        return adminRepository.findByUsername(username);
    }

    @Override
    public List<Admin> findAll(String username, int page, int pageSize) {
        Pageable pageable = PageRequest.of(page, pageSize);
        return adminRepository.findAllByUsernameContaining(username, pageable).getContent();
    }

    @Override
    public Optional<Admin> findById(Long id) {
        return adminRepository.findById(id);
    }

    @Override
    public void deleteById(Long id) {
        adminRepository.deleteById(id);
    }

    @Override
    public ResponseEntity<ResponseData> loginProcess(HttpServletRequest request, String username, String password) {
        Admin admin = findByUsername(username);
        HashMap<Object, Object> data = new HashMap<>();
        if (admin == null) {
            return ResponseUtils.response(RetEnum.RET_USER_NOT_EXIST, data);
        }
        if (!admin.getPassword().equals(password)) {
            return ResponseUtils.response(RetEnum.RET_USER_PASSWORD_ERROR, data);
        }
        if (!admin.getEnabled()) {
            return ResponseUtils.response(RetEnum.RET_USER_DISABLED, data);
        }
        try {
            String userAgent = request.getHeader("User-Agent");
            if (userAgent != null && userAgent.length() > 255) {
                userAgent = userAgent.substring(0, 255);
            }
            admin.setLastLoginIP(CommUtils.getClientIP(request));
            saveAdmin(admin);
            log.info("AdminServiceImpl ==> loginProcess ... [ {}{} ]", username, "登入成功");
            JwtUtils jwtToken = new JwtUtils();
            String token = jwtToken.generateToken(admin); // 取得token
            data.put("token", token);
            return ResponseUtils.response(RetEnum.RET_SUCCESS, data, "登入成功");
        } catch (Exception e) {
            log.info("AdminServiceImpl ==> loginProcess ... [ {} ] Exception:{}", "登入失敗", e.toString());
            return ResponseUtils.response(RetEnum.RET_LOGIN_FAIL, data);
        }

    }

    @Override
    public ResponseEntity<ResponseData> loginOutProcess(String token) {
        new JwtUtils().invalidateToken(token);
        return ResponseUtils.response(RetEnum.RET_SUCCESS);
    }

    @Override
    public ResponseEntity<ResponseData> getInfoProcess(String token) {
        String username = new JwtUtils().verifyToken(token);
        Admin admin = findByUsername(username);
        HashMap<Object, Object> data = new HashMap<>();
        data.put("info", admin);
        return ResponseUtils.response(RetEnum.RET_SUCCESS, data);
    }

    @Override
    public ResponseEntity<ResponseData> getAllAdminsProcess(int page, int pageSize,
            String input) {
        HashMap<Object, Object> data = new HashMap<>();
        List<Admin> adminList = findAll(input, page, pageSize);
        Object pager = CommUtils.Pager(page, pageSize, adminList.size());
        data.put("list", adminList);
        data.put("pager", pager);
        return ResponseUtils.response(RetEnum.RET_SUCCESS, data);
    }

    @Override
    public ResponseEntity<ResponseData> updateAdminProcess(AdminDTO adminDTO) {
        
        Admin admin = findById(adminDTO.getId()).get();
        admin.setUsername(adminDTO.getUsername());
        admin.setPassword(adminDTO.getPassword());
        admin.setEnabled(adminDTO.getEnabled());
        admin.setRoles(adminDTO.getRoles());
        saveAdmin(admin);
        log.info("AdminServiceImpl ==> updateAdminProcess ... [ {} ]", "done");
        return ResponseUtils.response(RetEnum.RET_SUCCESS, "編輯成功");
    }

    @Override
    public ResponseEntity<ResponseData> deleteAdminProcess(Map<String, String> requestData) {
        String[] ids = requestData.get(requestData.keySet().toArray()[0]).split(",");
        for (String id : ids) {
            deleteById(Long.parseLong(id));
        }
        return ResponseUtils.response(RetEnum.RET_SUCCESS, "刪除成功");
    }

    @Override
    public ResponseEntity<ResponseData> addAdminProcess(HttpServletRequest request, AdminDTO adminDTO) {
        String adminDTOUsername = adminDTO.getUsername();
        Admin username = findByUsername(adminDTOUsername);

        if (username != null) {
            log.info("AdminServiceImpl ==> addAdminProcess ... 用戶已經存在 [ {} ]", adminDTOUsername);
            return ResponseUtils.response(RetEnum.RET_USER_EXIST);
        }

        Admin admin = new Admin();
        // List<String> roles = Arrays.asList(new String[] { "ADMIN" });
        admin.setUsername(adminDTOUsername);
        admin.setPassword(adminDTO.getPassword());
        admin.setEnabled(adminDTO.getEnabled());
        admin.setRoles(adminDTO.getRoles());
        admin.setRegIp(CommUtils.getClientIP(request));
        admin.setLastLoginIP(CommUtils.getClientIP(request));
        saveAdmin(admin);
        log.info("AdminServiceImpl ==> addAdminProcess ... [ {} ] 新增成功", adminDTOUsername);
        return ResponseUtils.response(RetEnum.RET_SUCCESS, "新增成功");
    }

    @Override
    public ResponseEntity<ResponseData> passwordChangeProcess(AdminDTO adminDTO) {

        Long id = adminDTO.getId();
        String password = adminDTO.getPassword();
        Admin admin = findById(id).get();
        admin.setPassword(password);
        saveAdmin(admin);
        return ResponseUtils.response(RetEnum.RET_SUCCESS, "修改密碼成功，即將跳轉到登入頁面");
    }
}
