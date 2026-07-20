package com.boosterhub.team.domain;

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
@Table(name = "coach_team_assignments")
public class CoachTeamAssignment {

    @Id
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "coach_person_id", nullable = false)
    private Person coachPerson;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "team_id", nullable = false)
    private Team team;

    @Column(name = "created_at", nullable = false)
    private OffsetDateTime createdAt;

    protected CoachTeamAssignment() {
        // Required by JPA.
    }

    public CoachTeamAssignment(
            UUID id,
            Person coachPerson,
            Team team,
            OffsetDateTime createdAt
    ) {
        this.id = id;
        this.coachPerson = coachPerson;
        this.team = team;
        this.createdAt = createdAt;
    }

    public UUID getId() {
        return id;
    }

    public Person getCoachPerson() {
        return coachPerson;
    }

    public Team getTeam() {
        return team;
    }

    public OffsetDateTime getCreatedAt() {
        return createdAt;
    }
}
