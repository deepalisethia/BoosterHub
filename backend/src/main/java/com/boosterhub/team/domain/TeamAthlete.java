package com.boosterhub.team.domain;

import com.boosterhub.athlete.domain.Athlete;
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
@Table(name = "team_athletes")
public class TeamAthlete {

    @Id
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "team_id", nullable = false)
    private Team team;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "athlete_id", nullable = false)
    private Athlete athlete;

    @Column(nullable = false)
    private boolean active;

    @Column(name = "joined_at", nullable = false)
    private OffsetDateTime joinedAt;

    protected TeamAthlete() {
        // Required by JPA.
    }

    public TeamAthlete(
            UUID id,
            Team team,
            Athlete athlete,
            boolean active,
            OffsetDateTime joinedAt
    ) {
        this.id = id;
        this.team = team;
        this.athlete = athlete;
        this.active = active;
        this.joinedAt = joinedAt;
    }

    public UUID getId() {
        return id;
    }

    public Team getTeam() {
        return team;
    }

    public Athlete getAthlete() {
        return athlete;
    }

    public boolean isActive() {
        return active;
    }

    public OffsetDateTime getJoinedAt() {
        return joinedAt;
    }
}