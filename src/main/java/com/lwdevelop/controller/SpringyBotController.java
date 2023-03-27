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

    @PostMapping("/start")
    private synchronized ResponseEntity<ResponseUtils.ResponseData> start(@RequestBody SpringyBotDTO springyBotDTO) {
        return springyBotService.start(springyBotDTO);
    }

    @PostMapping("/stop")
    private synchronized ResponseEntity<ResponseUtils.ResponseData> stop(@RequestBody SpringyBotDTO springyBotDTO) {
        return springyBotService.stop(springyBotDTO);
    }

    @PostMapping("/addBot")
    public ResponseEntity<ResponseUtils.ResponseData> addBot(
            @RequestBody SpringyBotDTO springyBotDTO) throws Exception {

        return springyBotService.addBot(springyBotDTO);
    }

    @PostMapping("/getAllBot")
    public ResponseEntity<ResponseUtils.ResponseData> getAllBot(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int pageSize) throws Exception {

        return springyBotService.getAllBot(page, pageSize);
    }

    @PostMapping("/deleteBot")
    public ResponseEntity<ResponseUtils.ResponseData> deleteBot(
            @RequestBody Map<String, String> requestData) throws Exception {

        return springyBotService.deleteBot(requestData);
    }

    @PostMapping("/updateBot")
    public ResponseEntity<ResponseUtils.ResponseData> updateBot(
            @RequestBody SpringyBotDTO springyBotDTO) throws Exception {

        return springyBotService.updateBot(springyBotDTO);
    }

    // @GetMapping("/hello")
    // public String test(){
    // Custom c = new Custom("5855785269:AAH9bvPpYudd2wSAvMnBTiKakCeoB92_Z_8",
    // "CCP_1121_BOT", new DefaultBotOptions());
    // c.sendTextMsg("null", "-1001700543954");
    // return "fuck";
    // }
}
