package com.optimagrowth.license;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.core.env.Environment;

import javax.annotation.PostConstruct;

@SpringBootApplication
@RefreshScope
@RequiredArgsConstructor
@Slf4j
public class LicensingServiceApplication {

    private final Environment environment;


    @PostConstruct
    public void logApplicationProperties() {
        log.info("Password: {}", environment.getProperty("spring.datasource.password"));
        log.info("username: {}", environment.getProperty("spring.datasource.username"));
        log.info("url: {}", environment.getProperty("spring.datasource.url"));
        log.info("url: {}", environment.getProperty("spring.datasource.url"));
        log.info("eureka url: {}", environment.getProperty("eureka.instance.client.service-url.default-zone"));
    }
    public static void main(String[] args) {
        SpringApplication.run(LicensingServiceApplication.class, args);

    }

}
