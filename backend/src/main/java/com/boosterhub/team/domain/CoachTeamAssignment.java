package com.boosterhub.team.domain;

import com.boosterhub.user.domain.User;
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
    @JoinColumn(name = "coach_user_id", nullable = false)
    private User coachUser;

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
            User coachUser,
            Team team,
            OffsetDateTime createdAt
    ) {
        this.id = id;
        this.coachUser = coachUser;
        this.team = team;
        this.createdAt = createdAt;
    }

    public UUID getId() {
        return id;
    }

    public User getCoachUser() {
        return coachUser;
    }

    public Team getTeam() {
        return team;
    }

    public OffsetDateTime getCreatedAt() {
        return createdAt;
    }
}