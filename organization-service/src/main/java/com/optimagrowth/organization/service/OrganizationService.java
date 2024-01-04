package com.optimagrowth.organization.service;

import java.util.Optional;
import java.util.UUID;

import brave.ScopedSpan;
import brave.Tracer;
import com.optimagrowth.organization.events.SimpleKafkaSourceBean;
import com.optimagrowth.organization.model.ActionEnum;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import com.optimagrowth.organization.model.Organization;
import com.optimagrowth.organization.repository.OrganizationRepository;

@Slf4j
@Service
@RequiredArgsConstructor
public class OrganizationService {

    private final OrganizationRepository repository;
    private final SimpleKafkaSourceBean simpleKafkaSourceBean;
    private final Tracer tracer;

    public Organization findById(String organizationId) {
        Optional<Organization> opt;
        ScopedSpan newSpan = tracer.startScopedSpan("getOrgDBCall");
        try {
            opt = repository.findById(organizationId);
            simpleKafkaSourceBean.publishOrganizationChange(ActionEnum.GET, organizationId);
            if (opt.isEmpty()) {
                String message = String.format("Unable to find an organization with theOrganization id %s", organizationId);
                log.error(message);
                throw new IllegalArgumentException(message);
            }
            log.debug("Retrieving Organization Info: " + opt.get());

        } finally {
            newSpan.tag("peer.service", "postgres");
            newSpan.annotate("Client received");
            newSpan.finish();
        }
        return opt.get();
    }

    public Organization create(Organization organization){
    	organization.setId( UUID.randomUUID().toString());
        organization = repository.save(organization);

        simpleKafkaSourceBean.publishOrganizationChange(ActionEnum.CREATED, organization.getId());
        return organization;

    }

    public void update(Organization organization){
    	repository.save(organization);
    }

    public void delete(String organizationId){
    	repository.deleteById(organizationId);
    }
}