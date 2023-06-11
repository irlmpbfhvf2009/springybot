package com.lwdevelop.controller;

import java.net.InetAddress;
import java.net.UnknownHostException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.lwdevelop.service.impl.TestServiceImpl;
import com.lwdevelop.utils.ResponseUtils;
import com.lwdevelop.utils.RetEnum;

@RestController
@RequestMapping("/debug")
public class TestController {

    @Autowired
    private TestServiceImpl testServiceImpl;

    @PostMapping("/addConfig")
    public ResponseEntity<ResponseUtils.ResponseData> addConfig(@RequestParam("id") Long id) throws Exception {
        return testServiceImpl.addConfig(id);
    }

    @PostMapping("/getIP")
    public ResponseEntity<ResponseUtils.ResponseData> getIP() throws Exception {
        try {
            InetAddress localhost = InetAddress.getLocalHost();
            String localhostIp = localhost.getHostAddress();

            // 獲取當前服務器的IP地址
            String serverIp = InetAddress.getLocalHost().getHostAddress();
            String str = "";
            // 判斷當前是本地還是服務器上啟動的
            if (serverIp.equals("127.0.0.1")) {
                str = "服務器啟動";
            } else {
                str = "本地啟動";
            }
            return ResponseUtils.response(RetEnum.RET_SUCCESS,
                    str + " localhostIp : " + localhostIp + " serverIp : " + serverIp);
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
        return null;
    }

}
