package org.springybot.controller;

import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springybot.dto.AdminDTO;
import org.springybot.service.impl.AdminServiceImpl;
import org.springybot.utils.ResponseUtils;
// import io.swagger.annotations.Api;
// import io.swagger.annotations.ApiOperation;

@Controller
// @Api(tags = "管理員操作")
@RequestMapping("/admins")
public class AdminPanelController {

    @Autowired
    private AdminServiceImpl adminService;

    /**
     * 管理員登入
     * 
     * 透過提供的使用者名稱和密碼進行管理員登入。
     * 
     * @param request  用於處理 HTTP 請求的 HttpServletRequest 物件
     * @param username 使用者名稱，來自請求的 username 參數
     * @param password 密碼，來自請求的 password 參數
     * @return 包含登入回應的 ResponseEntity 物件
     * @throws Exception 登入過程中發生錯誤時拋出異常
     * @version v1
     * @since June 11, 2023
     * @author Leo
     */
    // @ApiOperation(value = "登入", notes = "處理管理員登入請求")
    @PostMapping("/v1/login")
    public ResponseEntity<ResponseUtils.ResponseData> login(
            HttpServletRequest request,
            @RequestParam("username") String username,
            @RequestParam("password") String password) throws Exception {

        return adminService.loginProcess(request, username, password);
    }

    /**
     * 管理員登出
     * 
     * 登出目前已登入的管理員。
     * 
     * @param token 身份驗證令牌，來自請求的 token 參數
     * @return 包含登出回應的 ResponseEntity 物件
     * @throws Exception 登出過程中發生錯誤時拋出異常
     * @version v1
     * @since June 11, 2023
     * @author Leo
     */
    // @ApiOperation(value = "登出", notes = "處理管理員登出請求")
    @PostMapping("/v1/loginOut")
    public ResponseEntity<ResponseUtils.ResponseData> loginOut(
            @RequestParam("token") String token) throws Exception {

        return adminService.loginOutProcess(token);
    }

    /**
     * 獲取管理員資訊
     * 
     * 根據提供的身份驗證令牌，獲取當前管理員的資訊。
     * 
     * @param token 身份驗證令牌，來自請求的 token 參數
     * @return 包含管理員資訊的 ResponseEntity 物件
     * @throws Exception 獲取資訊過程中發生錯誤時拋出異常
     * @version v1
     * @since June 11, 2023
     * @author Leo
     */
    // @ApiOperation(value = "管理員資訊", notes = "獲取管理員資訊")
    @PostMapping("/v1/info")
    public ResponseEntity<ResponseUtils.ResponseData> getInfoApi(
            @RequestParam("token") String token) throws Exception {
        return adminService.getInfoProcess(token);
    }

    /**
     * 獲取所有管理員列表
     * 
     * 根據提供的分頁參數和關鍵字，獲取所有管理員的列表。
     * 
     * @param page     頁碼，預設值為 0，來自請求的 page 參數
     * @param pageSize 每頁大小，預設值為 10，來自請求的 pageSize 參數
     * @param input    關鍵字，來自請求的 input 參數
     * @return 包含管理員列表的 ResponseEntity 物件
     * @throws Exception 獲取列表過程中發生錯誤時拋出異常
     * @version v1
     * @since June 11, 2023
     * @author Leo
     */
    // @ApiOperation(value = "獲取所有管理員", notes = "分頁獲取管理員列表")
    @PostMapping("/v1/getAllAdmins")
    public ResponseEntity<ResponseUtils.ResponseData> getAllAdmins(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int pageSize,
            @RequestParam("input") String input) throws Exception {

        return adminService.getAllAdminsProcess(page, pageSize, input);
    }

    /**
     * 更新管理員資訊
     * 
     * 根據提供的管理員資料，更新管理員的相關資訊。
     * 
     * @param adminDTO 包含更新資訊的 AdminDTO 物件，來自請求的 request body
     * @return 包含更新結果的 ResponseEntity 物件
     * @throws Exception 更新過程中發生錯誤時拋出異常
     * @version v1
     * @since June 11, 2023
     * @author Leo
     */
    // @ApiOperation(value = "更新管理員資訊", notes = "更新管理員的詳細資訊")
    @PostMapping("/v1/updateAdmin")
    public ResponseEntity<ResponseUtils.ResponseData> updateAdmin(
            @RequestBody AdminDTO adminDTO) throws Exception {

        return adminService.updateAdminProcess(adminDTO);
    }

    /**
     * 新增管理員
     * 
     * 根據提供的管理員資料，新增一個新的管理員。
     * 
     * @param request  用於處理 HTTP 請求的 HttpServletRequest 物件
     * @param adminDTO 包含新增資訊的 AdminDTO 物件，來自請求的 request body
     * @return 包含新增結果的 ResponseEntity 物件
     * @throws Exception 新增過程中發生錯誤時拋出異常
     * @version v1
     * @since June 11, 2023
     * @author Leo
     */
    // @ApiOperation(value = "新增管理員", notes = "向系統新增新的管理員")
    @PostMapping("/v1/addAdmin")
    public ResponseEntity<ResponseUtils.ResponseData> addAdmin(
            HttpServletRequest request,
            @RequestBody AdminDTO adminDTO) throws Exception {

        return adminService.addAdminProcess(request, adminDTO);
    }

    /**
     * 刪除管理員
     * 
     * 根據提供的請求資料，刪除指定的管理員。
     * 
     * @param requestData 包含刪除資訊的 Map 物件，來自請求的 request body
     * @return 包含刪除結果的 ResponseEntity 物件
     * @throws Exception 刪除過程中發生錯誤時拋出異常
     * @version v1
     * @since June 11, 2023
     * @author Leo
     */
    // @ApiOperation(value = "刪除管理員", notes = "刪除或批量刪除指定管理員")
    @PostMapping("/v1/deleteAdmins")
    public ResponseEntity<ResponseUtils.ResponseData> delAdmin(
            @RequestBody Map<String, String> requestData) throws Exception {

        return adminService.deleteAdminProcess(requestData);
    }

    /**
     * 修改管理員密碼
     * 
     * 根據提供的管理員資料，修改管理員的密碼。
     * 
     * @param adminDTO 包含密碼修改資訊的 AdminDTO 物件，來自請求的 request body
     * @return 包含密碼修改結果的 ResponseEntity 物件
     * @throws Exception 密碼修改過程中發生錯誤時拋出異常
     * @version v1
     * @since June 11, 2023
     * @author Leo
     */
    // @ApiOperation(value = "修改管理員密碼", notes = "修改指定管理員密碼")
    @PostMapping("/v1/passwordChange")
    public ResponseEntity<ResponseUtils.ResponseData> passwordChange(
            @RequestBody AdminDTO adminDTO) throws Exception {

        return adminService.passwordChangeProcess(adminDTO);
    }

}
