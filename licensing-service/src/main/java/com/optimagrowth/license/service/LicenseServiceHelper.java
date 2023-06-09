package com.optimagrowth.license.service;

import com.optimagrowth.license.model.License;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeoutException;

/**
 * @author: Ezekiel Eromosei
 * @created: 28 June 2023
 */

@Slf4j
@Component
public class LicenseServiceHelper {

    @CircuitBreaker(name = "licenseAlternateService", fallbackMethod = "buildFallbackAlternateLicenseList")
    public List<License> dummyService(String organizationId) throws IOException {
        sleep();
        return Collections.emptyList();
    }

    private List<License> buildFallbackAlternateLicenseList(String organizationId, Throwable t){
        System.out.println("In fallback method 2");
        License license = new License();
        license.setLicenseId("111111111-00-00000");
        license.setOrganizationId(organizationId);
        license.setProductName("Sorry no licensing information currently available XXXXXXXX");
        return List.of(license);
    }

    public static void sleep() throws IOException {
        try {
            Thread.sleep(5000);
            throw new IOException("IO Exception occurred");
        }catch (InterruptedException ex){
            log.error(ex.getMessage());
        }
    }
}
