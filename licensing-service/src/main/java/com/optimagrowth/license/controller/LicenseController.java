package com.optimagrowth.license.controller;

import com.optimagrowth.license.model.License;
import com.optimagrowth.license.service.LicenseService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Locale;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

/**
 * @author: Ezekiel Eromosei
 * @created: 28 March 2023
 */

@RestController
@RequestMapping(value = "v1/organization/{organizationId}/license")
@RequiredArgsConstructor
public class LicenseController {

    private final LicenseService licenseService;


    @GetMapping(value="/{licenseId}")
    public ResponseEntity<License> getLicense(@PathVariable("organizationId") String organizationId,
                                              @PathVariable("licenseId") String licenseId) {

        License license = licenseService.getLicense(licenseId, organizationId);

        license.add(linkTo(methodOn(LicenseController.class)
                .getLicense(organizationId, license.getLicenseId()))
                .withSelfRel(),
                linkTo(methodOn(LicenseController.class)
//                        .createLicense(organizationId, license, null))
                        .createLicense(license))
                        .withRel("createLicense"),
                linkTo(methodOn(LicenseController.class)
//                        .updateLicense(organizationId, license))
                        .updateLicense(license))
                        .withRel("updateLicense"),
                linkTo(methodOn(LicenseController.class)
//                        .deleteLicense(organizationId, license.getLicenseId()))
                        .deleteLicense(license.getLicenseId()))
                        .withRel("deleteLicense"));
        return ResponseEntity.ok(license);
    }


//    @PutMapping
//    public ResponseEntity<String> updateLicense(@PathVariable("organizationId") String organizationId, @RequestBody License request) {
//        return ResponseEntity.ok(licenseService.updateLicense(request, organizationId));
//    }
//
//    @PostMapping
//    public ResponseEntity<String> createLicense(@PathVariable("organizationId") String organizationId,
//                                                @RequestBody License request,
//                                                @RequestHeader(value = "Accept-Language", required = false) Locale locale) {
//        return new ResponseEntity<>(licenseService.createLicense(request, organizationId, locale), HttpStatus.CREATED);
//    }
//
//    @DeleteMapping(value="/{licenseId}")
//    public ResponseEntity<String> deleteLicense(@PathVariable("organizationId") String organizationId,
//                                                @PathVariable("licenseId") String licenseId) {
//        return ResponseEntity.ok(licenseService.deleteLicense(licenseId, organizationId));
//    }

    @PutMapping
    public ResponseEntity<License> updateLicense(@RequestBody License request) {
        return ResponseEntity.ok(licenseService.updateLicense(request));
    }

    @PostMapping
    public ResponseEntity<License> createLicense(@RequestBody License request) {
        return ResponseEntity.ok(licenseService.createLicense(request));
    }

    @DeleteMapping(value="/{licenseId}")
    public ResponseEntity<String> deleteLicense(@PathVariable("licenseId") String licenseId) {
        return ResponseEntity.ok(licenseService.deleteLicense(licenseId));
    }

    @DeleteMapping(value="/{licenseId}/{clientType}")
    public ResponseEntity<License> getLicensesWithClient(@PathVariable("organizationId") String organizationId,
                                                @PathVariable("licenseId") String licenseId,
                                                @PathVariable("clientType") String clientType) {
        return ResponseEntity.ok(licenseService.getLicense(organizationId, licenseId, clientType));
    }
}
