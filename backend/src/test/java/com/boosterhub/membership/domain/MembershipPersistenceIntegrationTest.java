package com.boosterhub.membership.domain;

import com.boosterhub.organization.domain.Organization;
import com.boosterhub.person.domain.Person;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.time.OffsetDateTime;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@Transactional
class MembershipPersistenceIntegrationTest {

    @Autowired
    private EntityManager entityManager;

    @Test
    void persistsAndReloadsMembershipWithStatus() {
        Organization organization = newOrganization();
        Person person = newPerson();
        Membership membership = newMembership(organization, person, MembershipStatus.ACTIVE);

        entityManager.persist(organization);
        entityManager.persist(person);
        entityManager.persist(membership);
        entityManager.flush();
        entityManager.clear();

        Membership loaded = entityManager.find(Membership.class, membership.getId());

        assertEquals(MembershipStatus.ACTIVE, loaded.getStatus());
        assertEquals(organization.getId(), loaded.getOrganization().getId());
        assertEquals(person.getId(), loaded.getPerson().getId());
    }

    @ParameterizedTest
    @EnumSource(MembershipStatus.class)
    void mapsEachApprovedStatus(MembershipStatus status) {
        Organization organization = newOrganization();
        Person person = newPerson();
        Membership membership = newMembership(organization, person, status);

        entityManager.persist(organization);
        entityManager.persist(person);
        entityManager.persist(membership);
        entityManager.flush();
        entityManager.clear();

        Membership loaded = entityManager.find(Membership.class, membership.getId());

        assertEquals(status, loaded.getStatus());
    }

    @Test
    void rejectsASecondMembershipForTheSamePersonInTheSameOrganization() {
        Organization organization = newOrganization();
        Person person = newPerson();
        Membership first = newMembership(organization, person, MembershipStatus.ACTIVE);

        entityManager.persist(organization);
        entityManager.persist(person);
        entityManager.persist(first);
        entityManager.flush();

        Membership duplicate = newMembership(organization, person, MembershipStatus.INVITED);
        entityManager.persist(duplicate);

        assertThrows(PersistenceException.class, entityManager::flush);
    }

    @Test
    void allowsTheSamePersonToHaveMembershipsInDifferentOrganizations() {
        Person person = newPerson();
        Organization firstOrganization = newOrganization();
        Organization secondOrganization = newOrganization();
        Membership firstMembership = newMembership(firstOrganization, person, MembershipStatus.ACTIVE);
        Membership secondMembership = newMembership(secondOrganization, person, MembershipStatus.INVITED);

        entityManager.persist(person);
        entityManager.persist(firstOrganization);
        entityManager.persist(secondOrganization);
        entityManager.persist(firstMembership);
        entityManager.persist(secondMembership);
        entityManager.flush();
        entityManager.clear();

        Membership loadedFirst = entityManager.find(Membership.class, firstMembership.getId());
        Membership loadedSecond = entityManager.find(Membership.class, secondMembership.getId());

        assertEquals(MembershipStatus.ACTIVE, loadedFirst.getStatus());
        assertEquals(MembershipStatus.INVITED, loadedSecond.getStatus());
    }

    private static Organization newOrganization() {
        OffsetDateTime now = OffsetDateTime.now();
        return new Organization(
                UUID.randomUUID(),
                "Test Booster Organization " + UUID.randomUUID(),
                "Test High School",
                "Mascots",
                true,
                now,
                now
        );
    }

    private static Person newPerson() {
        OffsetDateTime now = OffsetDateTime.now();
        return new Person(UUID.randomUUID(), "Jordan", "Rivera", null, now, now);
    }

    private static Membership newMembership(Organization organization, Person person, MembershipStatus status) {
        return new Membership(UUID.randomUUID(), organization, person, status, OffsetDateTime.now());
    }
}
