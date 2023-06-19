package com.optimagrowth.license.config.client;

import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

/**
 * @author: Ezekiel Eromosei
 * @created: 19 June 2023
 */

@Configuration
public class LoadBalancedRestTemplateConfig {

    @LoadBalanced // gets a list of all the organization instances
    @Bean
    public RestTemplate restTemplate(){
        return new RestTemplate();
    }
}
