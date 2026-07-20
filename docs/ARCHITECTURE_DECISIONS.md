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

---

## ADR-007: Product Domain Modeling Philosophy

**Status:** Accepted (Sprint 2)

**Context:** Sprint 1 modeled the domain starting from backend entities and
a flat role field (admin, coach, parent, athlete). Revisiting that
modeling from the customer's side, rather than the schema's side, surfaced
that it conflated who a person is with what they do and with what they're
allowed to act as within an organization. Before further domain modeling
continues, the philosophy governing how people, activities, and access are
represented needed to be settled.

**Decision:** BoosterHub is an administrative operating platform for
booster organizations — not a coaching, athlete-performance, recruiting,
or practice-management platform. The initial customer is a single Booster
Organization, and the architecture is intended to support multiple booster
organizations belonging to the same school.

People and their participation are modeled as follows:

- Person is the stable human identity.
- Parent, Athlete, and Coach are contextual forms of participation.
- Athlete is represented through an organization-specific Athlete record
  belonging to Membership.
- Parent and Coach participation are expressed through explicit
  Parent–Athlete and Coach–Team relationships.
- Volunteer is an activity, not an identity or Position.
- Membership, Position, and Permission are separate concepts.
- Positions are assigned through Membership.
- Permissions attach to Positions, not directly to Person or Membership.
- Member is a display label for an active Membership without a named
  Position; it is not itself a Position.

BoosterHub provides a set of built-in positions (President, Vice
President, Treasurer, Secretary, Board Member, Coach), and organizations
may additionally define their own custom positions. BoosterHub defines the
fixed set of available permissions; organizations assign those permissions
to positions, not to individual people.

**Alternatives Considered:** Treating the school or athletic department as
the customer, with booster organizations as a subordinate concept.
Modeling volunteering, fundraising, and similar activities as user roles
alongside Parent, Coach, and Athlete. Allowing organizations to invent
their own permissions rather than only assigning from a set BoosterHub
defines. Continuing with a single flat role per person instead of
separating Person, Membership, contextual participation, Position, and
Permission.

**Consequences:** This philosophy sets the direction for all future
domain modeling: Person, Membership, contextual participation, Position,
and Permission must remain distinct concepts — Person as the stable human
identity, Membership as the link between Person and Organization, Parent,
Athlete, and Coach as contextual participation rather than identity,
Position as organizational responsibility, and Permission as authorized
capability. An activity like volunteering must never be conflated with
contextual participation or identity. The cost is that domain modeling now
requires more upfront thought than a single role field did, and Sprint 1's flat
role model will need to be reconciled with this philosophy once that
modeling work happens. This ADR intentionally stops short of specifying
how these concepts are represented in the database, the API, or any
entity — that is future architecture and domain-modeling work, to be
captured in its own ADR when it happens.

---

## ADR-008: Transition from User and Role through staged domain alignment

**Status:** Accepted (Sprint 3)

**Context:** The Sprint 1 backend represents human identity with `User`
and stores `ADMIN`, `COACH`, `PARENT`, and `ATHLETE` in
`OrganizationMembership.role`. The approved Domain Model now separates:

- Person as stable human identity.
- Membership as the Person–Organization relationship.
- Athlete, Parent–Athlete, and Coach–Team as contextual participation.
- Position as organizational responsibility.
- Permission as authorized capability.

The existing model must transition without editing prior Flyway
migrations, introducing unnecessary compatibility infrastructure, or
changing every domain relationship in one unreviewable step. BoosterHub
currently has no production users, no authentication, no membership or
athlete seed data, and no write endpoints for the affected concepts.

**Decision:** BoosterHub will use a three-phase, forward-only migration.
V1–V3 remain immutable. Each phase must leave the application buildable
and Hibernate validation aligned with the migrated schema.

**Phase 1 — Establish Person**

- Rename `User` to `Person`.
- Rename the `user` capability package to `person`.
- Rename the `users` table to `people` through a new Flyway migration.
- Rename affected foreign-key columns from user terminology to person
  terminology.
- Treat Person email as optional contact information, not login identity.
- Remove global active status from Person; organization participation
  belongs to Membership.
- Temporarily retain `Role` as an explicitly documented legacy concept.
- Do not add Account, authentication, or authorization.

**Phase 2 — Normalize Membership and contextual participation**

- Establish one Membership per Person–Organization pair.
- Replace the membership active flag with lifecycle status: `INVITED`,
  `PENDING_APPROVAL`, `ACTIVE`, `INACTIVE`.
- Make Athlete belong to Membership.
- Make Parent–Athlete reference the parent's Membership.
- Make Coach–Team reference the coach's Membership.
- Remove `OrganizationMembership.role` and the `Role` enum.
- Do not infer missing Athlete, Parent, or Coach relationships from legacy
  role labels.
- Preserve legacy data only when its meaning is unambiguous.
- Fail clearly when unexpected legacy data cannot be migrated safely
  rather than silently guessing or discarding it.

**Phase 3 — Establish Position and Permission**

- Begin only after the initial Permission catalog is approved from
  documented user journeys.
- Add organization-scoped Positions.
- Add the BoosterHub-owned Permission catalog.
- Add Membership–Position assignments.
- Add Position–Permission assignments.
- Seed approved built-in Positions for existing Organizations.
- Prohibit assigning Permissions directly to Person or Membership.
- Keep authentication and authorization enforcement in their separately
  approved implementation phase.

A Coach–Team relationship establishes contextual coaching participation
and team scope; a Coach Position grants organizational authority. The two
concepts may coexist but have different responsibilities. Login remains
required for application access, but Account is a separate future concept
from Person.

**Alternatives Considered:**

1. Replace User, Role, Membership, Athlete, Position, Permission, and all
   relationships in one migration. Rejected because it creates an
   unnecessarily large, difficult-to-review change.
2. Introduce a parallel identity/access model with dual reads and dual
   writes. Rejected because BoosterHub has no production identity data
   requiring zero-downtime compatibility.
3. Extend or rename the Role enum. Rejected because it would preserve the
   conflation between participation and authority.
4. Edit V1–V3. Rejected because Flyway migrations are immutable historical
   records.

**Consequences:** The migration remains reviewable and testable in small
phases. Existing schema history remains intact. Role temporarily remains
during Phase 1 as explicit technical debt. Multiple Flyway migrations and
coordinated entity updates are required. Unexpected ambiguous legacy data
will stop migration rather than be silently changed. Account,
authentication, authorization enforcement, and the complete Permission
catalog remain deferred. Every phase must include matching schema
changes, entity mappings, and relevant tests.
