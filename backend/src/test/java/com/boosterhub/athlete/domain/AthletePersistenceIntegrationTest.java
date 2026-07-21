package com.boosterhub.athlete.domain;

import com.boosterhub.membership.domain.Membership;
import com.boosterhub.membership.domain.MembershipStatus;
import com.boosterhub.organization.domain.Organization;
import com.boosterhub.person.domain.Person;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.time.OffsetDateTime;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
@Transactional
class AthletePersistenceIntegrationTest {

    @Autowired
    private EntityManager entityManager;

    @Test
    void persistsAndReloadsAthleteThroughMembership() {
        Organization organization = newOrganization();
        Person person = newPerson();
        Membership membership = newMembership(organization, person, MembershipStatus.ACTIVE);
        Athlete athlete = newAthlete(membership, 2028, true);

        entityManager.persist(organization);
        entityManager.persist(person);
        entityManager.persist(membership);
        entityManager.persist(athlete);
        entityManager.flush();
        entityManager.clear();

        Athlete loaded = entityManager.find(Athlete.class, athlete.getId());

        assertEquals(membership.getId(), loaded.getMembership().getId());
        assertEquals(2028, loaded.getGraduationYear());
        assertTrue(loaded.isActive());
    }

    @Test
    void reachesPersonAndOrganizationContextThroughMembership() {
        Organization organization = newOrganization();
        Person person = newPerson();
        Membership membership = newMembership(organization, person, MembershipStatus.ACTIVE);
        Athlete athlete = newAthlete(membership, 2027, true);

        entityManager.persist(organization);
        entityManager.persist(person);
        entityManager.persist(membership);
        entityManager.persist(athlete);
        entityManager.flush();
        entityManager.clear();

        Athlete loaded = entityManager.find(Athlete.class, athlete.getId());

        assertEquals(person.getId(), loaded.getMembership().getPerson().getId());
        assertEquals(organization.getId(), loaded.getMembership().getOrganization().getId());
    }

    @Test
    void rejectsASecondAthleteForTheSameMembership() {
        Organization organization = newOrganization();
        Person person = newPerson();
        Membership membership = newMembership(organization, person, MembershipStatus.ACTIVE);
        Athlete first = newAthlete(membership, 2026, true);

        entityManager.persist(organization);
        entityManager.persist(person);
        entityManager.persist(membership);
        entityManager.persist(first);
        entityManager.flush();

        Athlete duplicate = newAthlete(membership, 2029, true);
        entityManager.persist(duplicate);

        assertThrows(PersistenceException.class, entityManager::flush);
    }

    @Test
    void keepsAthleteActiveIndependentOfMembershipStatus() {
        Organization organization = newOrganization();
        Person person = newPerson();
        Membership membership = newMembership(organization, person, MembershipStatus.INACTIVE);
        Athlete athlete = newAthlete(membership, 2030, true);

        entityManager.persist(organization);
        entityManager.persist(person);
        entityManager.persist(membership);
        entityManager.persist(athlete);
        entityManager.flush();
        entityManager.clear();

        Athlete loaded = entityManager.find(Athlete.class, athlete.getId());

        assertEquals(MembershipStatus.INACTIVE, loaded.getMembership().getStatus());
        assertTrue(loaded.isActive());
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

    private static Athlete newAthlete(Membership membership, Integer graduationYear, boolean active) {
        OffsetDateTime now = OffsetDateTime.now();
        return new Athlete(UUID.randomUUID(), membership, graduationYear, active, now, now);
    }
}
