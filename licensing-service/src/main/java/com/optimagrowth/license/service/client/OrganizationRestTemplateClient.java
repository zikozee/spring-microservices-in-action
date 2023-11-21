package com.optimagrowth.license.service.client;

import com.optimagrowth.license.model.Organization;
import com.optimagrowth.license.service.SecurityContextUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.OAuth2AuthenticatedPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
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
    @Qualifier(value = "all")
    private final RestTemplate allRestTemplate;

    private final SecurityContextUtil securityContextUtil;

    public Organization getOrganization(String organizationId){

        HttpEntity http = new HttpEntity(securityContextUtil.jwtHttpHeaders());
//        ResponseEntity<Organization> restExchange = restTemplate
//                .exchange("http://organization-service/v1/organization/{organizationId}", HttpMethod.GET,
//                        http, Organization.class, organizationId);

        //using gateway
        ResponseEntity<Organization> restExchange = allRestTemplate
                .exchange("http://localhost:8072/organization/v1/organization/{organizationId}", HttpMethod.GET,
                        http, Organization.class, organizationId);
        return restExchange.getBody();
    }
}
