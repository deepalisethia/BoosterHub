-- ADR-008 Phase 2A: normalize Membership. Replaces the legacy active
-- flag and Role with MembershipStatus. Role values are read only to
-- validate that the explicit relationships they imply already exist on
-- record; they are never used to create Athlete, Parent-Athlete,
-- Coach-Team, Position, or Permission rows. Any legacy data that cannot
-- be migrated safely stops the migration instead of being guessed at or
-- discarded.

DO $$
DECLARE
    duplicate_count INTEGER;
BEGIN
    SELECT COUNT(*) INTO duplicate_count
    FROM (
        SELECT organization_id, person_id
        FROM organization_memberships
        GROUP BY organization_id, person_id
        HAVING COUNT(*) > 1
    ) duplicates;

    IF duplicate_count > 0 THEN
        RAISE EXCEPTION
            'Phase 2A migration blocked: % Person-Organization pair(s) have more than one membership row. Manual review is required before this migration can proceed.',
            duplicate_count;
    END IF;
END $$;

DO $$
DECLARE
    admin_count INTEGER;
BEGIN
    SELECT COUNT(*) INTO admin_count
    FROM organization_memberships
    WHERE role = 'ADMIN';

    IF admin_count > 0 THEN
        RAISE EXCEPTION
            'Phase 2A migration blocked: % ADMIN membership(s) found. Phase 2 has no safe authority equivalent for ADMIN; manual review is required before this migration can proceed.',
            admin_count;
    END IF;
END $$;

DO $$
DECLARE
    missing_athlete_count INTEGER;
BEGIN
    SELECT COUNT(*) INTO missing_athlete_count
    FROM organization_memberships m
    WHERE m.role = 'ATHLETE'
      AND NOT EXISTS (
          SELECT 1
          FROM athletes a
          WHERE a.person_id = m.person_id
            AND a.organization_id = m.organization_id
      );

    IF missing_athlete_count > 0 THEN
        RAISE EXCEPTION
            'Phase 2A migration blocked: % ATHLETE membership(s) have no matching Athlete record in the same Organization. Manual review is required before this migration can proceed.',
            missing_athlete_count;
    END IF;
END $$;

DO $$
DECLARE
    missing_parent_relationship_count INTEGER;
BEGIN
    SELECT COUNT(*) INTO missing_parent_relationship_count
    FROM organization_memberships m
    WHERE m.role = 'PARENT'
      AND NOT EXISTS (
          SELECT 1
          FROM parent_athlete_relationships par
          JOIN athletes a ON a.id = par.athlete_id
          WHERE par.parent_person_id = m.person_id
            AND a.organization_id = m.organization_id
      );

    IF missing_parent_relationship_count > 0 THEN
        RAISE EXCEPTION
            'Phase 2A migration blocked: % PARENT membership(s) have no matching Parent-Athlete relationship in the same Organization. Manual review is required before this migration can proceed.',
            missing_parent_relationship_count;
    END IF;
END $$;

DO $$
DECLARE
    missing_coach_assignment_count INTEGER;
BEGIN
    SELECT COUNT(*) INTO missing_coach_assignment_count
    FROM organization_memberships m
    WHERE m.role = 'COACH'
      AND NOT EXISTS (
          SELECT 1
          FROM coach_team_assignments cta
          JOIN teams t ON t.id = cta.team_id
          WHERE cta.coach_person_id = m.person_id
            AND t.organization_id = m.organization_id
      );

    IF missing_coach_assignment_count > 0 THEN
        RAISE EXCEPTION
            'Phase 2A migration blocked: % COACH membership(s) have no matching Coach-Team assignment in the same Organization. Manual review is required before this migration can proceed.',
            missing_coach_assignment_count;
    END IF;
END $$;

ALTER TABLE organization_memberships ADD COLUMN status VARCHAR(50);

UPDATE organization_memberships
    SET status = CASE WHEN active THEN 'ACTIVE' ELSE 'INACTIVE' END;

ALTER TABLE organization_memberships ALTER COLUMN status SET NOT NULL;

ALTER TABLE organization_memberships
    ADD CONSTRAINT chk_membership_status
    CHECK (status IN ('INVITED', 'PENDING_APPROVAL', 'ACTIVE', 'INACTIVE'));

ALTER TABLE organization_memberships DROP CONSTRAINT chk_membership_role;

ALTER TABLE organization_memberships DROP CONSTRAINT uq_membership_person_role;

ALTER TABLE organization_memberships
    ADD CONSTRAINT uq_membership_person UNIQUE (organization_id, person_id);

ALTER TABLE organization_memberships DROP COLUMN role;

ALTER TABLE organization_memberships DROP COLUMN active;
