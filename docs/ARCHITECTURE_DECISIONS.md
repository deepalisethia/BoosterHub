# Architecture Decision Records

This file holds the timeless record of BoosterHub's significant architecture
decisions — what was decided, stated plainly, without the narrative around
it. Each ADR is written once and does not change after the fact; if a
decision is later reversed, that reversal gets its own new ADR that
supersedes the old one, and the old one is marked superseded rather than
edited or deleted.

For the story behind a decision — what alternatives we explored, what
discussion led to it, what we got wrong along the way — see the
corresponding sprint in
[`ENGINEERING_JOURNAL.md`](./ENGINEERING_JOURNAL.md). This file answers
"what did we decide"; the journal answers "why were we even talking about
this."

Each ADR follows the same shape:

- **Status** — Proposed, Accepted, or Superseded (with a link to whatever
  superseded it).
- **Context** — the situation that forced the decision.
- **Decision** — what we chose, stated as a single clear sentence.
- **Alternatives Considered** — what else was on the table.
- **Consequences** — what this costs us, not just what it buys us.

---

## ADR-001: Organize packages by business capability, not technical layer

**Status:** Accepted (Sprint 1)

**Context:** BoosterHub is a modular monolith with multiple features
(organizations, users, teams, athletes) that will keep growing. Package
structure needed to be decided before the first line of feature code was
written, since it's expensive to restructure later.

**Decision:** Packages are organized by business capability
(`organization`, `user`, `team`, `athlete`, …), and each capability contains
its own `api`, `application`, `domain`, and `infrastructure` sub-packages.

**Alternatives Considered:** A traditional layered structure, with
top-level `controllers`, `services`, and `repositories` packages shared
across the whole application.

**Consequences:** Feature boundaries stay visible by inspection, and a
capability could be extracted into its own service later without first
untangling it from unrelated code. The cost is some duplication of
structural boilerplate (each capability repeats the same four
sub-packages), which is an acceptable trade for clearer ownership.

---

## ADR-002: Flyway owns the database schema

**Status:** Accepted (Sprint 1)

**Context:** The application needed a strategy for managing schema
evolution from day one, before any table existed.

**Decision:** All schema changes are made through versioned Flyway
migrations. No other mechanism is permitted to create or alter schema.

**Alternatives Considered:** Letting Hibernate generate or update the
schema automatically from entity mappings.

**Consequences:** Every schema change is explicit, reviewable SQL and part
of version control history. The cost is that entity changes require a
matching migration to be written by hand — there is no auto-generation
shortcut.

---

## ADR-003: Hibernate runs in `ddl-auto=validate` mode only

**Status:** Accepted (Sprint 1)

**Context:** Given ADR-002, Hibernate still needs some `ddl-auto` setting.
The options that don't involve Hibernate writing to the schema are
`none` and `validate`.

**Decision:** `spring.jpa.hibernate.ddl-auto=validate`. Hibernate checks
entity mappings against the live schema at startup and refuses to start
if they disagree; it never issues DDL itself.

**Alternatives Considered:** `ddl-auto=none` (no check at all), and the
mutating modes (`update`, `create-drop`) ruled out by ADR-002.

**Consequences:** Mapping drift is caught immediately, at boot, rather
than surfacing later as a runtime error on first query. The cost is that
adding a new entity field always requires writing the migration first —
skipping it fails fast instead of silently working.

---

## ADR-004: Model domain relationships as explicit, distinct tables

**Status:** Accepted (Sprint 1)

**Context:** BoosterHub's domain has several distinct relationship types —
organization membership, team rosters, coach-team assignments, and
parent-athlete relationships — each with different cardinality and
integrity rules.

**Decision:** Each relationship type gets its own table, with its own
foreign keys, `UNIQUE`, and `CHECK` constraints, rather than a single
generic relationship table.

**Alternatives Considered:** One polymorphic `relationships` table with a
`type` discriminator column and nullable foreign keys to whichever
entities were involved.

**Consequences:** The database enforces referential integrity and
uniqueness rules directly, instead of pushing that responsibility into
application code. The cost is more tables and more repository/entity
classes than a single generic table would require.

---

## ADR-005: Athlete records may exist without a linked user account

**Status:** Accepted (Sprint 1)

**Context:** Booster organizations typically build out an athlete roster
before every athlete (or their parent) has created a login.

**Decision:** `athletes.user_id` is nullable; an `Athlete` record does not
require an associated `User`.

**Alternatives Considered:** Requiring a `User` account to exist before an
`Athlete` record can be created.

**Consequences:** Rostering can proceed independently of identity
onboarding, matching how these organizations actually operate. The cost is
that any code reading `Athlete.getUser()` must handle the null case rather
than assuming it's always populated.

---

## ADR-006: Never serialize JPA entities directly in REST responses

**Status:** Accepted (Sprint 1)

**Decision:** Every REST endpoint returns a dedicated response type (a
Java record in the feature's `api` package), never a JPA entity.

**Context:** The first REST endpoint (`GET /api/organizations`) needed a
response shape, forcing this decision before a precedent existed.

**Alternatives Considered:** Returning JPA entities directly and letting
Jackson serialize them.

**Consequences:** The API contract stays decoupled from the database
schema, and there's no risk of leaking lazy-loading proxies or
internal-only fields. The cost is a mapping method (entity → response)
that has to be written and kept in sync for every endpoint.
