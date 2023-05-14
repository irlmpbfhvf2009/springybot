package com.lwdevelop.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.lwdevelop.service.impl.TestServiceImpl;
import com.lwdevelop.utils.ResponseUtils;

@RestController
@RequestMapping("/debug")
public class TestController {
    
    @Autowired
    private TestServiceImpl testServiceImpl;
    
    @PostMapping("/addConfig")
    public ResponseEntity<ResponseUtils.ResponseData> addConfig(@RequestParam("id") Long id) throws Exception {
        return testServiceImpl.addConfig(id);
    }
    
}
