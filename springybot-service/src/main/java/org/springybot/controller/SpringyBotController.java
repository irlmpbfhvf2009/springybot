package org.springybot.controller;

import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springybot.dto.ConfigDTO;
import org.springybot.dto.SpringyBotDTO;
import org.springybot.service.impl.SpringyBotServiceImpl;
import org.springybot.utils.RedisUtils;
import org.springybot.utils.ResponseUtils;

@RestController
@RequestMapping("/springybot")
public class SpringyBotController {

    @Autowired
    private RedisUtils redisUtils;

    @Autowired
    private SpringyBotServiceImpl springyBotService;

    // RPC
    @PostMapping("/v1/cacheSpringyBotDataToRedis")
    public void cacheSpringyBotDataToRedis(@RequestBody String token) throws Exception {
        springyBotService.cacheSpringyBotDataToRedis(token);
    }

    /**
     * 新增 SpringyBot
     * 
     * 根據提供的 SpringyBot 資料，新增一個 SpringyBot。
     * 
     * @param springyBotDTO 包含 SpringyBot 資訊的 SpringyBotDTO 物件，來自請求的 request body
     * @return 包含新增結果的 ResponseEntity 物件
     * @throws Exception 新增 SpringyBot 過程中發生錯誤時拋出異常
     * @version v1
     * @since June 11, 2023
     * @author Leo
     */
    @PostMapping("/v1/addBot")
    public ResponseEntity<ResponseUtils.ResponseData> addBot(
            @RequestBody SpringyBotDTO springyBotDTO) throws Exception {

        return springyBotService.addBot(springyBotDTO);
    }

    /**
     * 獲取所有 SpringyBot
     * 
     * 獲取所有已存在的 SpringyBot。
     * 
     * @param page     頁數，默認值為 0，從第一頁開始
     * @param pageSize 每頁數量，默認值為 10
     * @return 包含 SpringyBot 列表的 ResponseEntity 物件
     * @throws Exception 獲取 SpringyBot 過程中發生錯誤時拋出異常
     * @version v1
     * @since June 11, 2023
     * @author Leo
     */
    @PostMapping("/v1/getAllBot")
    public ResponseEntity<ResponseUtils.ResponseData> getAllBot(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int pageSize) throws Exception {

        return springyBotService.getAllBot(page, pageSize);
    }

    /**
     * 刪除 SpringyBot
     * 
     * 根據提供的請求數據，刪除指定的 SpringyBot。
     * 
     * @param requestData 包含請求數據的 Map 物件，來自請求的 request body
     * @return 包含刪除結果的 ResponseEntity 物件
     * @throws Exception 刪除 SpringyBot 過程中發生錯誤時拋出異常
     * @version v1
     * @since June 11, 2023
     * @author Leo
     */
    @PostMapping("/v1/deleteBot")
    public ResponseEntity<ResponseUtils.ResponseData> deleteBot(
            @RequestBody Map<String, String> requestData) throws Exception {

        return springyBotService.deleteBot(requestData);
    }

    /**
     * 更新 SpringyBot
     * 
     * 根據提供的 SpringyBotDTO 對象，更新指定的 SpringyBot。
     * 
     * @param springyBotDTO 包含 SpringyBot 訊息的 SpringyBotDTO 對象，來自請求的 request body
     * @return 包含更新結果的 ResponseEntity 物件
     * @throws Exception 更新 SpringyBot 過程中發生錯誤時拋出異常
     * @version v1
     * @since June 11, 2023
     * @author Leo
     */
    @PostMapping("/v1/updateBot")
    public ResponseEntity<ResponseUtils.ResponseData> updateBot(
            @RequestBody SpringyBotDTO springyBotDTO) throws Exception {

        return springyBotService.updateBot(springyBotDTO);
    }

    /**
     * 更新 Config
     * 
     * 根據提供的 ConfigDTO 對象，更新指定的 Config。
     * 
     * @param ConfigDTO 包含 Config。 訊息的 ConfigDTO 對象，來自請求的 request body
     * @return 包含更新結果的 ResponseEntity 物件
     * @throws Exception 更新 Config。 過程中發生錯誤時拋出異常
     * @version v1
     * @since June 29, 2023
     * @author Leo
     */
    @PostMapping("/v1/updateConfig")
    public ResponseEntity<ResponseUtils.ResponseData> updateConfig(
            @RequestBody ConfigDTO configDTO) throws Exception {

        return springyBotService.updateConfig(configDTO);
    }

    /**
     * 查詢機器人管理的頻道
     * 
     * 根據提供的 configId 對象，查詢相關資料。
     * 
     * @param configId Config 對象的 id，用於查詢關聯表中springybot.getConfig()，來自請求的 request
     *                 body
     * @return 包含查詢結果的 ResponseEntity 物件
     * @throws Exception 抓取 chatId。 過程中發生錯誤時拋出異常
     * @version v1
     * @since June 29, 2023
     * @author Leo
     */
    @PostMapping("/v1/fetchManagedChat")
    public ResponseEntity<ResponseUtils.ResponseData> fetchManagedChatIds(
            @RequestBody ConfigDTO configDTO) throws Exception {
        return springyBotService.fetchManagedChat(configDTO);
    }

    @GetMapping("/clearCache")
    public String clearCache() throws Exception {
        redisUtils.clearAllData();
        return "clearCache";
    }
}
