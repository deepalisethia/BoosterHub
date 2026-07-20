-- ADR-008 Phase 1: establish Person terminology across the existing
-- relationships. OrganizationMembership and Role remain temporarily;
-- Membership normalization and replacement of Role are deferred to
-- Phase 2.

ALTER TABLE users RENAME TO people;

ALTER TABLE people RENAME CONSTRAINT users_pkey TO people_pkey;

ALTER TABLE people RENAME COLUMN email TO contact_email;

ALTER TABLE people DROP CONSTRAINT users_email_key;

ALTER TABLE people ALTER COLUMN contact_email DROP NOT NULL;

ALTER TABLE people DROP COLUMN active;

ALTER TABLE organization_memberships RENAME COLUMN user_id TO person_id;

ALTER TABLE organization_memberships
    RENAME CONSTRAINT fk_membership_user TO fk_membership_person;

ALTER TABLE organization_memberships
    RENAME CONSTRAINT uq_membership_user_role TO uq_membership_person_role;

ALTER INDEX idx_memberships_user RENAME TO idx_memberships_person;

ALTER TABLE athletes RENAME COLUMN user_id TO person_id;

ALTER TABLE athletes RENAME CONSTRAINT fk_athlete_user TO fk_athlete_person;

ALTER TABLE athletes RENAME CONSTRAINT uq_athlete_user TO uq_athlete_person;

ALTER TABLE parent_athlete_relationships
    RENAME COLUMN parent_user_id TO parent_person_id;

ALTER TABLE parent_athlete_relationships
    RENAME CONSTRAINT fk_parent_relationship_user TO fk_parent_relationship_person;

ALTER TABLE coach_team_assignments
    RENAME COLUMN coach_user_id TO coach_person_id;

ALTER TABLE coach_team_assignments
    RENAME CONSTRAINT fk_coach_assignment_user TO fk_coach_assignment_person;
