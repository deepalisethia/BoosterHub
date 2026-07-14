CREATE TABLE organizations (
    id UUID PRIMARY KEY,
    name VARCHAR(150) NOT NULL,
    school_name VARCHAR(150),
    mascot VARCHAR(100),
    active BOOLEAN NOT NULL DEFAULT TRUE,
    created_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE users (
    id UUID PRIMARY KEY,
    first_name VARCHAR(100) NOT NULL,
    last_name VARCHAR(100) NOT NULL,
    email VARCHAR(255) NOT NULL UNIQUE,
    active BOOLEAN NOT NULL DEFAULT TRUE,
    created_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE organization_memberships (
    id UUID PRIMARY KEY,
    organization_id UUID NOT NULL,
    user_id UUID NOT NULL,
    role VARCHAR(50) NOT NULL,
    active BOOLEAN NOT NULL DEFAULT TRUE,
    created_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT fk_membership_organization
        FOREIGN KEY (organization_id)
        REFERENCES organizations(id),

    CONSTRAINT fk_membership_user
        FOREIGN KEY (user_id)
        REFERENCES users(id),

    CONSTRAINT uq_membership_user_role
        UNIQUE (organization_id, user_id, role),

    CONSTRAINT chk_membership_role
        CHECK (role IN ('ADMIN', 'COACH', 'PARENT', 'ATHLETE'))
);

CREATE TABLE teams (
    id UUID PRIMARY KEY,
    organization_id UUID NOT NULL,
    name VARCHAR(150) NOT NULL,
    category VARCHAR(30) NOT NULL,
    active BOOLEAN NOT NULL DEFAULT TRUE,
    created_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT fk_team_organization
        FOREIGN KEY (organization_id)
        REFERENCES organizations(id),

    CONSTRAINT chk_team_category
        CHECK (category IN ('BOYS', 'GIRLS', 'COED')),

    CONSTRAINT uq_team_name
        UNIQUE (organization_id, name)
);

CREATE TABLE athletes (
    id UUID PRIMARY KEY,
    organization_id UUID NOT NULL,
    user_id UUID,
    first_name VARCHAR(100) NOT NULL,
    last_name VARCHAR(100) NOT NULL,
    graduation_year INTEGER,
    active BOOLEAN NOT NULL DEFAULT TRUE,
    created_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT fk_athlete_organization
        FOREIGN KEY (organization_id)
        REFERENCES organizations(id),

    CONSTRAINT fk_athlete_user
        FOREIGN KEY (user_id)
        REFERENCES users(id),

    CONSTRAINT uq_athlete_user
        UNIQUE (organization_id, user_id)
);

CREATE TABLE team_athletes (
    id UUID PRIMARY KEY,
    team_id UUID NOT NULL,
    athlete_id UUID NOT NULL,
    active BOOLEAN NOT NULL DEFAULT TRUE,
    joined_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT fk_team_athlete_team
        FOREIGN KEY (team_id)
        REFERENCES teams(id),

    CONSTRAINT fk_team_athlete_athlete
        FOREIGN KEY (athlete_id)
        REFERENCES athletes(id),

    CONSTRAINT uq_team_athlete
        UNIQUE (team_id, athlete_id)
);

CREATE TABLE parent_athlete_relationships (
    id UUID PRIMARY KEY,
    parent_user_id UUID NOT NULL,
    athlete_id UUID NOT NULL,
    relationship_type VARCHAR(50),
    created_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT fk_parent_relationship_user
        FOREIGN KEY (parent_user_id)
        REFERENCES users(id),

    CONSTRAINT fk_parent_relationship_athlete
        FOREIGN KEY (athlete_id)
        REFERENCES athletes(id),

    CONSTRAINT uq_parent_athlete
        UNIQUE (parent_user_id, athlete_id)
);

CREATE TABLE coach_team_assignments (
    id UUID PRIMARY KEY,
    coach_user_id UUID NOT NULL,
    team_id UUID NOT NULL,
    created_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT fk_coach_assignment_user
        FOREIGN KEY (coach_user_id)
        REFERENCES users(id),

    CONSTRAINT fk_coach_assignment_team
        FOREIGN KEY (team_id)
        REFERENCES teams(id),

    CONSTRAINT uq_coach_team
        UNIQUE (coach_user_id, team_id)
);

CREATE INDEX idx_memberships_organization
    ON organization_memberships(organization_id);

CREATE INDEX idx_memberships_user
    ON organization_memberships(user_id);

CREATE INDEX idx_teams_organization
    ON teams(organization_id);

CREATE INDEX idx_athletes_organization
    ON athletes(organization_id);

CREATE INDEX idx_team_athletes_team
    ON team_athletes(team_id);

CREATE INDEX idx_parent_relationships_athlete
    ON parent_athlete_relationships(athlete_id);