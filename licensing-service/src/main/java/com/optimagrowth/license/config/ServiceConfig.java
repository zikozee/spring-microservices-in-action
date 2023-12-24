package com.optimagrowth.license.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * @author: Ezekiel Eromosei
 * @created: 25 April 2023
 */

@Getter
@Setter
@Configuration
@ConfigurationProperties(prefix = "example")
public class ServiceConfig {
    private String property;
    private String redisServer;
    private String redisPort;
}
