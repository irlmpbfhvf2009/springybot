package org.springybot.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/index")
public class IndexController {

    
    @GetMapping("/version")
    public String version() throws Exception {
        return "4.0.1";
    }

}
