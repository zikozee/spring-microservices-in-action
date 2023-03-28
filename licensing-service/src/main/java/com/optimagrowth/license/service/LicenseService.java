package com.optimagrowth.license.service;

import com.optimagrowth.license.model.License;
import org.springframework.stereotype.Service;

import java.util.Random;

/**
 * @author: Ezekiel Eromosei
 * @created: 28 March 2023
 */

@Service
public class LicenseService {
    public License getLicense(String licenseId, String organizationId){
        License license = new License();
        license.setId(new Random().nextInt(1000));
        license.setLicenseId(licenseId);
        license.setOrganizationId(organizationId);
        license.setDescription("Software product");
        license.setProductName("Ostock");
        license.setLicenseType("full");

        return license;
    }

    public String createLicense(License license, String organizationId){
        String responseMessage = null;
        if(license != null) {
            license.setOrganizationId(organizationId);
            responseMessage = "This is the post and the object is: " + license;
        }

        return responseMessage;
    }

    public String updateLicense(License license, String organizationId){
        String responseMessage = null;
        if (license != null) {
            license.setOrganizationId(organizationId);
            responseMessage = "This is the put and the object is: "+ license;
        }

        return responseMessage;
    }

    public String deleteLicense(String licenseId, String organizationId){
        return "Deleting license with id" + licenseId +" for the organization "+ organizationId;
    }
}
