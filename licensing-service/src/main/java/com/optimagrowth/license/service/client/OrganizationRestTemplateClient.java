package com.optimagrowth.license.service.client;

import com.optimagrowth.license.model.Organization;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;



/**
 * @author: Ezekiel Eromosei
 * @created: 19 June 2023
 */

@Component
@RequiredArgsConstructor
public class OrganizationRestTemplateClient {

    @Qualifier(value = "loadBalancedTemplate")
    private final RestTemplate restTemplate;

    public Organization getOrganization(String organizationId){

        ResponseEntity<Organization> restExchange = restTemplate
                .exchange("http://organization-service/v1/organization/{organizationId}", HttpMethod.GET,
                        null, Organization.class, organizationId);
        return restExchange.getBody();
    }
}
