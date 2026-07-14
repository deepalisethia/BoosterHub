package com.boosterhub.organization.api;

import com.boosterhub.organization.domain.Organization;

import java.util.UUID;

public record OrganizationResponse(
        UUID id,
        String name,
        String schoolName,
        String mascot,
        boolean active
) {

    public static OrganizationResponse from(Organization organization) {
        return new OrganizationResponse(
                organization.getId(),
                organization.getName(),
                organization.getSchoolName(),
                organization.getMascot(),
                organization.isActive()
        );
    }
}
