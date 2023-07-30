package org.springybot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@EnableDiscoveryClient // 開啟發現服務功能
@EnableFeignClients
@ComponentScan(basePackages = "org.springybot.*")
public class CoolbaoApplication {

    public static void main(String[] args) {
        SpringApplication.run(CoolbaoApplication.class, args);
    }

}
