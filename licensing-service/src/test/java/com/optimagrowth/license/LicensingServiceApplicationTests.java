package com.optimagrowth.license;

import com.optimagrowth.license.model.License;
import com.optimagrowth.license.repository.LicenseRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.UUID;

@SpringBootTest
class LicensingServiceApplicationTests {

    @Autowired
    private LicenseRepository licenseRepository;

    @Test
    void contextLoads() {
    }

    @Test
    void save() {
        License license = new License();
        license.setLicenseId(UUID.randomUUID().toString());
        license.setDescription("first");
        license.setOrganizationId("zimvy");
        license.setProductName("proddy");
        license.setLicenseType("licy");
        license.setComment("first one ");

        License save = licenseRepository.save(license);
        System.out.println( save.getOrganizationId().equalsIgnoreCase(license.getOrganizationId()));
    }
}
