package com.boosterhub.person.domain;

import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.time.OffsetDateTime;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

@SpringBootTest
@Transactional
class PersonPersistenceIntegrationTest {

    @Autowired
    private EntityManager entityManager;

    @Test
    void persistsPersonWithoutContactEmail() {
        Person person = newPerson("Jordan", "Rivera", null);

        entityManager.persist(person);
        entityManager.flush();
        entityManager.clear();

        Person loaded = entityManager.find(Person.class, person.getId());

        assertNull(loaded.getContactEmail());
    }

    @Test
    void allowsTwoPeopleToShareTheSameContactEmail() {
        String sharedEmail = "family@example.com";
        Person first = newPerson("Jordan", "Rivera", sharedEmail);
        Person second = newPerson("Sam", "Rivera", sharedEmail);

        entityManager.persist(first);
        entityManager.persist(second);
        entityManager.flush();
        entityManager.clear();

        Person loadedFirst = entityManager.find(Person.class, first.getId());
        Person loadedSecond = entityManager.find(Person.class, second.getId());

        assertEquals(sharedEmail, loadedFirst.getContactEmail());
        assertEquals(sharedEmail, loadedSecond.getContactEmail());
    }

    @Test
    void readsPersistedValuesAfterFlushAndClear() {
        Person person = newPerson("Taylor", "Nguyen", "taylor@example.com");

        entityManager.persist(person);
        entityManager.flush();
        entityManager.clear();

        Person loaded = entityManager.find(Person.class, person.getId());

        assertEquals("Taylor", loaded.getFirstName());
        assertEquals("Nguyen", loaded.getLastName());
        assertEquals("taylor@example.com", loaded.getContactEmail());
    }

    private static Person newPerson(String firstName, String lastName, String contactEmail) {
        OffsetDateTime now = OffsetDateTime.now();
        return new Person(UUID.randomUUID(), firstName, lastName, contactEmail, now, now);
    }
}
