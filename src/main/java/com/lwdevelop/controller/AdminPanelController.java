package com.lwdevelop.controller;

import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import com.lwdevelop.dto.AdminDTO;
import com.lwdevelop.service.impl.AdminServiceImpl;
import com.lwdevelop.utils.ResponseUtils;

@Controller
@RequestMapping("/admins")
public class AdminPanelController {
    
    @Autowired
    private AdminServiceImpl adminService;

    @GetMapping("/index")
    public String index() {
        return "index";
    }

    @PostMapping("/login")
    public ResponseEntity<ResponseUtils.ResponseData> login(
            HttpServletRequest request,
            @RequestParam("username") String username,
            @RequestParam("password") String password) throws Exception {

        return adminService.loginProcess(request, username, password);
    }

    @PostMapping("/loginOut")
    public ResponseEntity<ResponseUtils.ResponseData> loginOut(
            @RequestParam("token") String token) throws Exception {

        return adminService.loginOutProcess(token);
    }

    @PostMapping("/info")
    public ResponseEntity<ResponseUtils.ResponseData> getInfoApi(
            @RequestParam("token") String token) throws Exception {

        return adminService.getInfoProcess( token);
    }

    @PostMapping("/getAllAdmins")
    public ResponseEntity<ResponseUtils.ResponseData> getAllAdmins(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int pageSize,
            @RequestParam("input") String input) throws Exception {

        return adminService.getAllAdminsProcess(page, pageSize, input);
    }

    @PostMapping("/updateAdmin")
    public ResponseEntity<ResponseUtils.ResponseData> updateAdmin(
            @RequestBody AdminDTO adminDTO) throws Exception {

        return adminService.updateAdminProcess(adminDTO);
    }

    @PostMapping("/addAdmin")
    public ResponseEntity<ResponseUtils.ResponseData> addAdmin(
            HttpServletRequest request,
            @RequestBody AdminDTO adminDTO) throws Exception {

        return adminService.addAdminProcess(request, adminDTO);
    }

    @PostMapping("/delAdmin")
    public ResponseEntity<ResponseUtils.ResponseData> delAdmin(
            @RequestBody Map<String, String> requestData) throws Exception {

        return adminService.deleteAdminProcess(requestData);
    }

    @PostMapping("/passwordChange")
    public ResponseEntity<ResponseUtils.ResponseData> passwordChange(
            @RequestBody AdminDTO adminDTO) throws Exception {

        return adminService.passwordChangeProcess(adminDTO);
    }


}
