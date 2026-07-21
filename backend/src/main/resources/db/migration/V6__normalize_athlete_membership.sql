-- ADR-008 Phase 2B: normalize Athlete so it belongs to Membership.
-- Athlete no longer references Organization or Person directly; it
-- receives that context through its Membership. Any legacy data that
-- cannot be migrated safely stops the migration instead of being
-- guessed at or discarded.

DO $$
DECLARE
    null_person_count INTEGER;
BEGIN
    SELECT COUNT(*) INTO null_person_count
    FROM athletes
    WHERE person_id IS NULL;

    IF null_person_count > 0 THEN
        RAISE EXCEPTION
            'Phase 2B migration blocked: % Athlete row(s) have a null person_id. Manual review is required before this migration can proceed.',
            null_person_count;
    END IF;
END $$;

DO $$
DECLARE
    missing_membership_count INTEGER;
BEGIN
    SELECT COUNT(*) INTO missing_membership_count
    FROM athletes a
    WHERE (
        SELECT COUNT(*)
        FROM organization_memberships m
        WHERE m.organization_id = a.organization_id
          AND m.person_id = a.person_id
    ) <> 1;

    IF missing_membership_count > 0 THEN
        RAISE EXCEPTION
            'Phase 2B migration blocked: % Athlete row(s) do not have exactly one matching Membership for their Organization and Person. Manual review is required before this migration can proceed.',
            missing_membership_count;
    END IF;
END $$;

DO $$
DECLARE
    name_mismatch_count INTEGER;
BEGIN
    SELECT COUNT(*) INTO name_mismatch_count
    FROM athletes a
    JOIN people p ON p.id = a.person_id
    WHERE a.first_name IS DISTINCT FROM p.first_name
       OR a.last_name IS DISTINCT FROM p.last_name;

    IF name_mismatch_count > 0 THEN
        RAISE EXCEPTION
            'Phase 2B migration blocked: % Athlete row(s) have a first_name or last_name that differs from their linked Person. Manual review is required before this migration can proceed.',
            name_mismatch_count;
    END IF;
END $$;

DO $$
DECLARE
    cross_organization_count INTEGER;
BEGIN
    SELECT COUNT(*) INTO cross_organization_count
    FROM team_athletes ta
    JOIN teams t ON t.id = ta.team_id
    JOIN athletes a ON a.id = ta.athlete_id
    WHERE t.organization_id <> a.organization_id;

    IF cross_organization_count > 0 THEN
        RAISE EXCEPTION
            'Phase 2B migration blocked: % Team-Athlete assignment(s) connect a Team and Athlete from different Organizations. Manual review is required before this migration can proceed.',
            cross_organization_count;
    END IF;
END $$;

ALTER TABLE athletes ADD COLUMN membership_id UUID;

UPDATE athletes a
    SET membership_id = (
        SELECT m.id
        FROM organization_memberships m
        WHERE m.organization_id = a.organization_id
          AND m.person_id = a.person_id
    );

ALTER TABLE athletes ALTER COLUMN membership_id SET NOT NULL;

ALTER TABLE athletes
    ADD CONSTRAINT fk_athlete_membership
    FOREIGN KEY (membership_id)
    REFERENCES organization_memberships(id);

ALTER TABLE athletes
    ADD CONSTRAINT uq_athlete_membership UNIQUE (membership_id);

ALTER TABLE athletes DROP CONSTRAINT fk_athlete_organization;

ALTER TABLE athletes DROP CONSTRAINT fk_athlete_person;

ALTER TABLE athletes DROP CONSTRAINT uq_athlete_person;

DROP INDEX idx_athletes_organization;

ALTER TABLE athletes DROP COLUMN organization_id;

ALTER TABLE athletes DROP COLUMN person_id;

ALTER TABLE athletes DROP COLUMN first_name;

ALTER TABLE athletes DROP COLUMN last_name;
