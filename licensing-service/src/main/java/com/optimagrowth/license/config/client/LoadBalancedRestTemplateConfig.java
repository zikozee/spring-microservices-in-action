package com.optimagrowth.license.config.client;

import com.optimagrowth.license.utils.UserContextInterceptor;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;
import java.util.List;

/**
 * @author: Ezekiel Eromosei
 * @created: 19 June 2023
 */

@Configuration
public class LoadBalancedRestTemplateConfig {

    @LoadBalanced // gets a list of all the organization instances
    @Bean(name = "loadBalancedTemplate")
    public RestTemplate restTemplate(){
        RestTemplate restTemplate = new RestTemplate();
        List<ClientHttpRequestInterceptor> interceptors = restTemplate.getInterceptors();
        if(interceptors.isEmpty()) {
            restTemplate.setInterceptors(Collections.singletonList(new UserContextInterceptor()));
        }else {
            interceptors.add(new UserContextInterceptor());
            restTemplate.setInterceptors(interceptors);
        }

        return restTemplate;
    }

    @Bean(name = "all")
    public RestTemplate allRestTemplate(){
        RestTemplate restTemplate = new RestTemplate();
        List<ClientHttpRequestInterceptor> interceptors = restTemplate.getInterceptors();
        if(interceptors.isEmpty()) {
            restTemplate.setInterceptors(Collections.singletonList(new UserContextInterceptor()));
        }else {
            interceptors.add(new UserContextInterceptor());
            restTemplate.setInterceptors(interceptors);
        }

        return restTemplate;
    }
}
