package com.optimagrowth.license.service.client;

import com.optimagrowth.license.model.Organization;
import lombok.RequiredArgsConstructor;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.client.RestTemplate;

import java.util.List;

/**
 * @author: Ezekiel Eromosei
 * @created: 19 June 2023
 */

@FeignClient("organization-service")
public interface OrganizationFeignClient {

    @GetMapping(value = "/v1/organization/{organizationId}")
    Organization getOrganization(@PathVariable("organizationId") String organizationId);
}
