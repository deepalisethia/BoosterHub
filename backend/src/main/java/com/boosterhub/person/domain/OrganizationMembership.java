package com.boosterhub.person.domain;

import com.boosterhub.organization.domain.Organization;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

import java.time.OffsetDateTime;
import java.util.UUID;

/**
 * Temporary ADR-008 Phase 1 technical debt: retains the pre-Sprint-2
 * Role-based membership model, unchanged, so Phase 1 can establish Person
 * without also redesigning Membership. Phase 2 will move this concept to
 * its final capability and remove Role.
 */
@Entity
@Table(name = "organization_memberships")
public class OrganizationMembership {

    @Id
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "organization_id", nullable = false)
    private Organization organization;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "person_id", nullable = false)
    private Person person;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 50)
    private Role role;

    @Column(nullable = false)
    private boolean active;

    @Column(name = "created_at", nullable = false)
    private OffsetDateTime createdAt;

    protected OrganizationMembership() {
        // Required by JPA.
    }

    public OrganizationMembership(
            UUID id,
            Organization organization,
            Person person,
            Role role,
            boolean active,
            OffsetDateTime createdAt
    ) {
        this.id = id;
        this.organization = organization;
        this.person = person;
        this.role = role;
        this.active = active;
        this.createdAt = createdAt;
    }

    public UUID getId() {
        return id;
    }

    public Organization getOrganization() {
        return organization;
    }

    public Person getPerson() {
        return person;
    }

    public Role getRole() {
        return role;
    }

    public boolean isActive() {
        return active;
    }

    public OffsetDateTime getCreatedAt() {
        return createdAt;
    }
}
