package com.optimagrowth.license;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.core.env.Environment;

import jakarta.annotation.PostConstruct;

@SpringBootApplication
@RefreshScope
@RequiredArgsConstructor
@EnableFeignClients
@Slf4j
public class LicensingServiceApplication implements ApplicationContextAware {

    private static ApplicationContext context;

    private final Environment environment;

    @PostConstruct
    public void logApplicationProperties() {
        log.info("Password: {}", environment.getProperty("spring.datasource.password"));
        log.info("username: {}", environment.getProperty("spring.datasource.username"));
        log.info("url: {}", environment.getProperty("spring.datasource.url"));
        log.info("url: {}", environment.getProperty("spring.datasource.url"));
        log.info("eureka host: {}", environment.getProperty("EUREKA_HOST"));
        log.info("eureka url: {}", environment.getProperty("eureka.client.service-url.default-zone"));
    }
    public static void main(String[] args) {
        SpringApplication.run(LicensingServiceApplication.class, args);

    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        context = applicationContext;
    }

    public static ApplicationContext getContext(){
        return context;
    }
}
