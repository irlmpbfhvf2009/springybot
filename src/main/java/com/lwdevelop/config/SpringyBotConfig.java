package com.lwdevelop.config;

import org.springframework.context.annotation.Configuration;

import lombok.Data;

@Data
@Configuration
public class SpringyBotConfig {
    
    private String token;

    private String botUsername;

    private String botPath;

}
