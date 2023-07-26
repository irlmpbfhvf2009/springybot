package com.lwdevelop.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.lwdevelop.utils.RedisUtils;

@Controller
@RequestMapping("/index")
public class IndexController {

    @Autowired
    RedisUtils redisUtils;
    
    @GetMapping("/version")
    public String version() throws Exception {
        return "version";
    }

}
