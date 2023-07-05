package com.lwdevelop.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller("/")
public class IndexController {

    @RequestMapping("/index")
    public String index() throws Exception {
        return "index";
    }
    
    @GetMapping("/version")
    public String version() throws Exception {
        return "version";
    }

}
