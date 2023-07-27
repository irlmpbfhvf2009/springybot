package org.springybot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages="org.springybot.*")
public class SpringybotServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringybotServiceApplication.class, args);
    }


}
