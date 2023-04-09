package com.lwdevelop.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller("/")
public class IndexController {

        // 新增招聘信息
    @RequestMapping("/index")
    public String index() throws Exception {

        return "index";
    }
}
