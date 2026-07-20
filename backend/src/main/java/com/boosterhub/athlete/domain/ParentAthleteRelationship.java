package com.boosterhub.athlete.domain;

import com.boosterhub.person.domain.Person;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

import java.time.OffsetDateTime;
import java.util.UUID;

@Entity
@Table(name = "parent_athlete_relationships")
public class ParentAthleteRelationship {

    @Id
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "parent_person_id", nullable = false)
    private Person parentPerson;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "athlete_id", nullable = false)
    private Athlete athlete;

    @Column(name = "relationship_type", length = 50)
    private String relationshipType;

    @Column(name = "created_at", nullable = false)
    private OffsetDateTime createdAt;

    protected ParentAthleteRelationship() {
        // Required by JPA.
    }

    public ParentAthleteRelationship(
            UUID id,
            Person parentPerson,
            Athlete athlete,
            String relationshipType,
            OffsetDateTime createdAt
    ) {
        this.id = id;
        this.parentPerson = parentPerson;
        this.athlete = athlete;
        this.relationshipType = relationshipType;
        this.createdAt = createdAt;
    }

    public UUID getId() {
        return id;
    }

    public Person getParentPerson() {
        return parentPerson;
    }

    public Athlete getAthlete() {
        return athlete;
    }

    public String getRelationshipType() {
        return relationshipType;
    }

    public OffsetDateTime getCreatedAt() {
        return createdAt;
    }
}
