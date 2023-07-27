package org.springybot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication
@EnableDiscoveryClient // 開啟發現服務功能
@ComponentScan(basePackages = "org.springybot.*")
public class CoolbaoApplication {

    public static void main(String[] args) {
        SpringApplication.run(CoolbaoApplication.class, args);
    }

    @LoadBalanced // 使用负载均衡配置
    @Bean
    RestTemplate restTemplate() {
        return new RestTemplate();
    }

}
