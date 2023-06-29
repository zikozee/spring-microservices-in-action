package com.optimagrowth.license.service;

import com.optimagrowth.license.config.ServiceConfig;
import com.optimagrowth.license.model.License;
import com.optimagrowth.license.model.Organization;
import com.optimagrowth.license.repository.LicenseRepository;
import com.optimagrowth.license.resilience.CustomRetryException;
import com.optimagrowth.license.service.client.OrganizationDiscoveryClient;
import com.optimagrowth.license.service.client.OrganizationFeignClient;
import com.optimagrowth.license.service.client.OrganizationRestTemplateClient;
import io.github.resilience4j.bulkhead.annotation.Bulkhead;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.TimeoutException;

/**
 * @author: Ezekiel Eromosei
 * @created: 28 March 2023
 */

@Slf4j
@Service
@RequiredArgsConstructor
public class LicenseService {

    private final MessageSource messageSource;
    private final LicenseRepository licenseRepository;
    private final ServiceConfig serviceConfig;
    private final OrganizationDiscoveryClient organizationDiscoveryClient;
    private final OrganizationRestTemplateClient organizationRestTemplateClient;
    private final OrganizationFeignClient organizationFeignClient;
    private final LicenseServiceHelper licenseServiceHelper;

    private int counter;

    public License getLicense(String licenseId, String organizationId){
        License license = licenseRepository
                .findByOrganizationIdAndLicenseId(organizationId, licenseId);
        if (null == license) {
            throw new IllegalArgumentException(
                    String.format(messageSource.getMessage(
                                    "license.search.error.message", null, null),
                            licenseId, organizationId));
        }
        return license.withComment(serviceConfig.getProperty());
    }

    public License createLicense(License license){
        license.setLicenseId(UUID.randomUUID().toString());
        licenseRepository.save(license);
        return license.withComment(serviceConfig.getProperty());
    }

    public License updateLicense(License license){
        licenseRepository.save(license);
        return license.withComment(serviceConfig.getProperty());
    }

    public String deleteLicense(String licenseId){
        String responseMessage = null;
        License license = new License();
        license.setLicenseId(licenseId);
        licenseRepository.delete(license);
        responseMessage = String.format(messageSource.getMessage(
                "license.delete.message", null, null),licenseId);
        return responseMessage;
    }

    public License getLicense(String licenseId, String organizationId, String clientType){
        License license = licenseRepository.findByOrganizationIdAndLicenseId(organizationId, licenseId);
        if (null == license) {
            throw new IllegalArgumentException(String.format(messageSource.getMessage("license.search.error.message", null, null),licenseId, organizationId));
        }

        Organization organization = retrieveOrganizationInfo(organizationId, clientType);
        if (null != organization) {
            license.setOrganizationName(organization.getName());
            license.setContactName(organization.getContactName());
            license.setContactEmail(organization.getContactEmail());
            license.setContactPhone(organization.getContactPhone());
        }

        return license.withComment(serviceConfig.getProperty());
    }

    private Organization retrieveOrganizationInfo(String organizationId, String clientType) {
        Organization organization = null;

        switch (clientType) {
            case "feign":
                System.out.println("I am using the feign client");
                organization = organizationFeignClient.getOrganization(organizationId);
                break;
            case "rest":
                System.out.println("I am using the rest client");
                organization = organizationRestTemplateClient.getOrganization(organizationId);
                break;
            case "discovery":
                System.out.println("I am using the discovery client");
                organization = organizationDiscoveryClient.getOrganization(organizationId);
                break;
            default:
                organization = organizationRestTemplateClient.getOrganization(organizationId);
        }

        return organization;
    }

//    @CircuitBreaker(name = "licenseService", fallbackMethod = "buildFallbackLicenseList")
//    @Bulkhead(name = "bulkheadLicenseService", fallbackMethod = "buildFallbackLicenseList", type = Bulkhead.Type.THREADPOOL)// this uses the thread pool config
//    @Bulkhead(name = "bulkheadLicenseService", fallbackMethod = "buildFallbackLicenseList")// this uses the semaphore/default config
    @Retry(name = "retryLicenseService")
    public List<License> getLicensesByOrganization(String organizationId) throws TimeoutException {
        System.out.println("current retry time: " + LocalDateTime.now());
        Random rand = new Random();
        int random = rand.nextInt(4) + 1;
        System.out.println("random: " + random);
        if(random == 1) {
            License license = new License();
            license.setOrganizationId("BOOMSHAKASHAKA");
            return List.of(license);
        }

        randomRunLong(random);

        return licenseRepository.findAllByOrganizationId(organizationId);
    }

    public List<License> buildFallbackLicenseList(String organizationId, Throwable t) throws IOException {
        //Assuming this too can fail, this also should be wrapped with a circuit breaker: Code defensively
        System.out.println("In fallback method 1");
        License license = new License();
        license.setLicenseId("0000000-00-00000");
        license.setOrganizationId(organizationId);
        license.setProductName("Sorry no licensing information currently available");
        int random = new Random().nextInt(2) + 1;
        System.out.println("random: " + random);
        if(random == 2) return licenseServiceHelper.dummyService(organizationId);

        return List.of(license);
    }


    private void randomRunLong(int random) throws TimeoutException {
        System.out.println("retry: " + counter++);
        if(random == 3) throw new CustomRetryException("a custom exception thrown");
        if(random == 4) sleep();
    }

    private static void sleep() throws TimeoutException {
        try {
            Thread.sleep(3000);
            throw new TimeoutException("timeout occurred");
        }catch (InterruptedException ex){
            log.error(ex.getMessage());
        }
    }
}
