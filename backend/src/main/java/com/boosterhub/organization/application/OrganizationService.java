package com.boosterhub.organization.application;

import com.boosterhub.organization.api.OrganizationResponse;
import com.boosterhub.organization.infrastructure.OrganizationRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class OrganizationService {

    private final OrganizationRepository organizationRepository;

    public OrganizationService(OrganizationRepository organizationRepository) {
        this.organizationRepository = organizationRepository;
    }

    @Transactional(readOnly = true)
    public List<OrganizationResponse> getActiveOrganizations() {
        return organizationRepository.findByActiveTrueOrderByNameAsc().stream()
                .map(OrganizationResponse::from)
                .toList();
    }
}
