package com.lwdevelop.controller;

import java.util.Map;
import javax.annotation.Resource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import com.lwdevelop.dto.SpringyBotDTO;
import com.lwdevelop.service.impl.SpringyBotServiceImpl;
import com.lwdevelop.utils.ResponseUtils;

@RestController
@RequestMapping("/springybot")
public class SpringyBotController {

    @Resource
    private TelegramBotsApi telegramBotsApi;

    @Autowired
    private SpringyBotServiceImpl springyBotService;

    /**
     * 啟動 SpringyBot
     * 
     * 根據提供的 SpringyBot 資料，啟動 SpringyBot 服務。
     * 
     * @param springyBotDTO 包含 SpringyBot 資訊的 SpringyBotDTO 物件，來自請求的 request body
     * @return 包含啟動結果的 ResponseEntity 物件
     * @version v1
     * @since June 11, 2023
     * @author Leo
     */
    @PostMapping("/v1/start")
    private synchronized ResponseEntity<ResponseUtils.ResponseData> start(@RequestBody SpringyBotDTO springyBotDTO) {
        return springyBotService.start(springyBotDTO);
    }

    /**
     * 停止 SpringyBot
     * 
     * 根據提供的 SpringyBot 資料，停止 SpringyBot 服務。
     * 
     * @param springyBotDTO 包含 SpringyBot 資訊的 SpringyBotDTO 物件，來自請求的 request body
     * @return 包含停止結果的 ResponseEntity 物件
     * @version v1
     * @since June 11, 2023
     * @author Leo
     */
    @PostMapping("/v1/stop")
    private synchronized ResponseEntity<ResponseUtils.ResponseData> stop(@RequestBody SpringyBotDTO springyBotDTO) {
        return springyBotService.stop(springyBotDTO);
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

}
