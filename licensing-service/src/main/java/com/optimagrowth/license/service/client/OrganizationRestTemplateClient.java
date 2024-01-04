package com.optimagrowth.license.service.client;

import brave.ScopedSpan;
import brave.Tracer;
import com.optimagrowth.license.model.Organization;
import com.optimagrowth.license.service.SecurityContextUtil;
import com.optimagrowth.license.utils.CacheUtil;
import com.optimagrowth.license.utils.UserContext;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.concurrent.TimeUnit;


/**
 * @author: Ezekiel Eromosei
 * @created: 19 June 2023
 */

@Slf4j
@Component
@RequiredArgsConstructor
public class OrganizationRestTemplateClient {

    @Qualifier(value = "loadBalancedTemplate")
    private final RestTemplate restTemplate;
    @Qualifier(value = "all")
    private final RestTemplate allRestTemplate;
    private final SecurityContextUtil securityContextUtil;
    private final CacheUtil cacheUtil;

    @Autowired
    Tracer tracer;

    private Organization checkRedisCache(String organizationId){
        ScopedSpan newSpan = tracer.startScopedSpan("readLicensingDataFromRedis");
        try {
            return cacheUtil.getData(organizationId, Organization.class);
        }catch (Exception ex){
            log.error("Error encountered while trying to retrieve organization: {} check redis Cache. Exception: {}", organizationId, ex.getLocalizedMessage(), ex);
            return null;
        }finally {
            newSpan.tag("peer.service", "redis");
            newSpan.annotate("Client received");
            newSpan.finish();
        }
    }

    private void cacheOrganizationObject(Organization organization){
        try {
            cacheUtil.setGenericData(organization.getId(), organization, false, 60, TimeUnit.MINUTES);
        }catch (Exception ex){
            log.error("Unable to cache organization: {} in Redis Exception: {}", organization.getId(), ex.getLocalizedMessage(), ex);
        }
    }

    public Organization getOrganization(String organizationId){
        log.debug("In Licensing Service.getOrganization: {}", UserContext.CORRELATION_ID);

        Organization organization = checkRedisCache(organizationId);
        if (organization != null){
            log.debug("I have successfully retrieved an organization {} from the redis cache: {}", organizationId, organization);
            return organization;
        }

        log.debug("Unable to locate organization from the redis cache: {}.",organizationId);

        HttpEntity http = new HttpEntity(securityContextUtil.jwtHttpHeaders());
//        ResponseEntity<Organization> restExchange = restTemplate
//                .exchange("http://organization-service/v1/organization/{organizationId}", HttpMethod.GET,
//                        http, Organization.class, organizationId);

        //using gateway
        ResponseEntity<Organization> restExchange = allRestTemplate
                .exchange("http://localhost:8072/organization/v1/organization/{organizationId}", HttpMethod.GET,
                        http, Organization.class, organizationId);
        Organization body = restExchange.getBody();
        if(body != null){
            cacheOrganizationObject(body);
        }
        return body;
    }
}
