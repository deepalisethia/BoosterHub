package com.boosterhub.person.domain;

/**
 * Temporary ADR-008 Phase 1 technical debt: the pre-Sprint-2 flat role
 * model. Phase 2 removes Role after Membership and contextual
 * participation are normalized; Phase 3 introduces Position and
 * Permission. Do not add new values.
 */
@Deprecated(forRemoval = true)
public enum Role {
    ADMIN,
    COACH,
    PARENT,
    ATHLETE
}
