# Engineering Journal

**Current Version:** v0.0.1

## Current Status

**✅ Completed**
- Sprint 1 — Foundation

**🚧 In Progress**
- Seed Data and the First Visible Vertical Slice

We deliberately don't keep a forward-looking roadmap here. Real projects
rarely unfold the way they're planned, and a list of future sprint titles
tends to age into fiction within a month or two. This section only ever
states what's done and what's being worked on right now.

## Project Metrics

A cumulative view of the system's size. Each sprint appends a column here
rather than replacing the last one, so growth is visible at a glance without
reading every entry. Compare against that sprint's own **Sprint Metrics**
table to see what changed in that sprint specifically.

| Metric             | Sprint 1 |
|---------------------|----------|
| Domain Models        | 8        |
| REST Endpoints       | 1        |
| Controllers          | 1        |
| Services             | 1        |
| Repositories         | 1        |
| DTOs                 | 1        |
| Database Tables      | 8        |
| Flyway Migrations    | 2        |
| Passing Tests        | 1        |
| Frontend Screens     | 0        |
| Lines of Code (Java) | 772      |

---

This is a running record of how BoosterHub was built, sprint by sprint. It is
written the way an internal team journal would be written — not as marketing
copy and not as a changelog, but as a record of what we set out to do, the
decisions we made, what we got wrong, and what we learned along the way.

We use "we" throughout, even in a single-developer context, because the
intent is to document engineering reasoning the way a team would: decisions
that could be handed to a new teammate, or defended in a design review.

Entries are appended in order and are not rewritten after the fact. If a
later sprint reveals that an earlier decision was wrong, we say so in that
later sprint rather than editing history.

This journal is one of three documents in this repository, and each has a
different job: [`README.md`](../README.md) is the front door — what the
project is and where things stand today. This journal is the story — the
chronological, narrative record of how the system came to be, including the
false starts. [`ARCHITECTURE_DECISIONS.md`](./ARCHITECTURE_DECISIONS.md) is
the philosophy — significant decisions, stated plainly and permanently, with
the story behind them left here instead. When an entry below references an
ADR, that's the split: this journal explains why we were talking about
something that sprint; the ADR is the timeless record of what we concluded.

---

## How to read this journal

Each entry follows the same structure so that patterns across sprints are
easy to compare:

- **Goal** — what we intended to accomplish, stated narrowly.
- **Why This Matters** — the architectural or product reasoning behind
  doing this now, in this order.
- **Work Completed** — a factual list of what was implemented.
- **Engineering Decisions** — what we decided, with a pointer to the ADR
  that holds the full reasoning.
- **Architectural Conversations** — the discussion behind the sprint's
  biggest design question, not just its resolution.
- **Mistakes** — what we got wrong this sprint, stated plainly. "None this
  sprint" is a valid, honest answer.
- **Lessons Learned** — things we didn't know going in.
- **Developer Reflection** — a short, honest note on a tradeoff we made.
- **Temptations We Resisted** — a short list of the shortcuts we
  deliberately did not take.
- **Open Questions** — things we chose not to decide yet, and why deferring
  was the right call.
- **Current Architecture** — a snapshot diagram, included when the sprint
  changed the shape of the system.
- **What's Next** — the specific next milestone, not a full roadmap.
- **Sprint Metrics** — a small table tracking the system's raw size and
  shape, so growth is visible sprint over sprint.
- **Sprint Outcome** — one paragraph stating, plainly, where the system
  stands at the end of the sprint.
- **Confidence Level** — a rough, honest gauge of how solid each area of
  the system feels right now, not a score to optimize.

Each entry also opens with a small metadata block — version, engineering
time, and the commits it corresponds to — so the narrative stays anchored to
the actual repository history and not just to calendar dates.

Sprint entries are meant to stay short. Sprint 1 runs long because it's
laying down the whole foundation at once; a typical sprint from here on
should read closer to 300–700 words than 1,000+.

---

<!--
  SPRINT ENTRY TEMPLATE
  Copy everything between the markers below for each new sprint.
  Keep section headings and order identical across entries so the
  journal stays scannable across 30-50 sprints. Keep it short — most
  sprints should land around 300-700 words, not 1,000+.
-->

<!-- TEMPLATE START -->

## Sprint N: [Short Descriptive Title]

**Dates:** [YYYY-MM-DD – YYYY-MM-DD]
**Version:** [vX.Y.Z]
**Engineering Time:** [~n hours]
**Commits:** [commit hashes, or the branch/PR this sprint merged]

### Goal

[One to three sentences describing the narrow, concrete objective of this
sprint.]

### Why This Matters

[One or two short paragraphs on the architectural or product reasoning
behind doing this now. Answer "why this, why now," not "what."]

### Work Completed

- [Item]
- [Item]

### Engineering Decisions

[One line per decision: what we chose, and a pointer to the ADR with the
full reasoning. Only elaborate inline if there is no ADR for it.]

- [Decision, one line.] See ADR-00X.

### Architectural Conversations

[A short paragraph on the sprint's biggest open design question — what was
tempting, what changed our mind, what we concluded. Omit for sprints with
no real design tension.]

### Mistakes

[What we got wrong this sprint, stated plainly. "None this sprint." is a
valid entry — don't manufacture one to fill the section.]

### Lessons Learned

- [Something that surprised us or a false assumption we corrected.]

### Developer Reflection

[A few honest, conversational sentences about a tradeoff made this sprint —
what we resisted and why, not a restatement of what we built.]

### Temptations We Resisted

- [Shortcut, feature, or dependency we deliberately did not add.]

### Open Questions

- [Something we chose not to decide yet, and why deferring was the right
  call.]

### Current Architecture

```
[ASCII diagram — include only when this sprint changed the system's shape.]
```

### What's Next

[Name the specific next sprint and its focus. Be decisive, not vague.]

### Sprint Metrics

[This sprint's snapshot. Also append a matching column to the **Project
Metrics** table near the top of this file.]

| Metric             | Value |
|---------------------|-------|
| Domain Models        | [n]   |
| REST Endpoints       | [n]   |
| Controllers          | [n]   |
| Services             | [n]   |
| Repositories         | [n]   |
| DTOs                 | [n]   |
| Database Tables      | [n]   |
| Flyway Migrations    | [n]   |
| Passing Tests        | [n]   |
| Frontend Screens     | [n]   |
| Lines of Code (Java) | [n]   |

### Sprint Outcome

[One plain-spoken paragraph stating where the system stands at the end of
this sprint.]

### Confidence Level

[A rough, honest bar per area that's actually been touched. Add or drop
rows as the system grows — this isn't a fixed checklist.]

```
Architecture      [██████████] X/10
Domain Model      [██████████] X/10
API Design        [██████████] X/10
```

<!-- TEMPLATE END -->

---

*Entries begin below.*

---

## Project Philosophy

BoosterHub is intentionally being built the way a small engineering team
would build a production SaaS product, not the way a typical portfolio
project evolves.

The objective is not to implement features as quickly as possible. The
objective is to make deliberate architectural decisions that scale as the
application grows — and to leave a record of that reasoning behind, the way
a real team's design reviews and ADRs would.

This journal is not meant to be a highlight reel: we are as interested in
recording the moments we changed our minds as in recording what we got right
the first time. If a later sprint proves an earlier decision wrong, that
gets written down in the later sprint, in the open, rather than quietly
fixed and forgotten.

---

## Sprint 1: Foundations — Modular Architecture and the First Vertical Slice

**Dates:** 2026-07-13 – 2026-07-13
**Version:** v0.0.1
**Engineering Time:** ~4.5 hours
**Commits:** Not yet committed as of this entry

### Goal

Establish the modular monolith's package structure and prove one complete
vertical slice — a read-only REST endpoint — works end to end against a
real PostgreSQL database, not mocks.

### Why This Matters

BoosterHub's domain has several genuinely different relationship types —
organization membership, team rosters, coach assignments, parent-athlete
links — and getting that shape right early matters more here than in a
typical CRUD app. Collapsing them into one generic "relationship" concept
now would have been faster, but we'd have paid for it later in lost
referential integrity and application code re-deriving meaning the schema
should have guaranteed.

Organizing packages by business capability rather than technical layer
keeps each feature's boundary visible and leaves the option to extract a
module into its own service later without untangling it first. Proving
this out with a read-only endpoint, before any writes, meant that if
something in the foundation were wrong, we'd find out here — not three
modules and a write path downstream.

### Work Completed

- Established the modular monolith structure, packages organized by
  business capability rather than technical layer.
- Modeled the initial domain: `Organization`, `User`,
  `OrganizationMembership`, `Team`, `Athlete`, `TeamAthlete`,
  `CoachTeamAssignment`, `ParentAthleteRelationship`.
- Configured Flyway as the single source of truth for schema, Hibernate
  running in `ddl-auto=validate` mode.
- Verified entity mappings agree with the Flyway-managed schema by booting
  the application against a live PostgreSQL instance.
- Built the first complete vertical slice — database → repository →
  service → controller → response DTO — as a single read-only
  `GET /api/organizations` endpoint.
- Confirmed the application starts cleanly and the full Maven test suite
  passes.

### Engineering Decisions

- Organize packages by business capability, not technical layer. See
  [ADR-001](./ARCHITECTURE_DECISIONS.md#adr-001-organize-packages-by-business-capability-not-technical-layer).
- Flyway owns the schema; Hibernate runs in `ddl-auto=validate` only. See
  [ADR-002](./ARCHITECTURE_DECISIONS.md#adr-002-flyway-owns-the-database-schema)
  and
  [ADR-003](./ARCHITECTURE_DECISIONS.md#adr-003-hibernate-runs-in-ddl-autovalidate-mode-only).
- Model relationships as distinct tables, not one generic polymorphic
  table. See
  [ADR-004](./ARCHITECTURE_DECISIONS.md#adr-004-model-domain-relationships-as-explicit-distinct-tables).
- Make `athletes.user_id` nullable — rostering shouldn't require an
  existing login. See
  [ADR-005](./ARCHITECTURE_DECISIONS.md#adr-005-athlete-records-may-exist-without-a-linked-user-account).
- Never serialize entities directly; always return a response DTO. See
  [ADR-006](./ARCHITECTURE_DECISIONS.md#adr-006-never-serialize-jpa-entities-directly-in-rest-responses).

### Architectural Conversations

The sprint's real discussion wasn't code — it was whether relationships
(membership, rosters, coach assignments, parent-athlete links) should be
one generic `relationships` table with a `type` column, or distinct tables.
The generic version looked simpler on paper, but walking through concrete
rules — "a coach assignment must reference a coach and a team," "a
parent-athlete pairing must be unique" — made clear that a generic schema
pushes all of that enforcement into application code instead of the
database. We chose explicit tables so the schema enforces those rules
directly. Full record in ADR-004.

### Mistakes

`V1__create_identity_and_roster_tables.sql` was created and applied while
empty — the actual schema ended up in `V2` instead. Because Flyway had
already recorded `V1` as applied, we left it as-is rather than editing it
to "clean up" the history. The lesson: once a migration has been applied,
it doesn't get rewritten, even for a mistake this small — a corrected
follow-up migration is the only safe move, not an edit to the original.

### Lessons Learned

- `ddl-auto=validate` only proves itself once you actually boot against the
  real schema — we confirmed today it catches mapping drift at startup,
  not just in theory.
- The pull toward a generic relationships table shows up early, even at
  this size. Resisting it now was cheap; unwinding it later, with more
  modules depending on it, would not have been.

### Developer Reflection

We talked more than once today about just adding authentication — a login
flow would have made the day feel more finished. We resisted it. One
validated vertical slice is worth more than three partially built features,
and the goal wasn't to impress ourselves with visible progress. It was to
establish a pattern we can repeat with confidence.

### Temptations We Resisted

- Authentication
- CRUD endpoints (create/update/delete for organizations)

### Open Questions

- How should nested relationships (a team's roster, an athlete's team
  history) be shaped in response DTOs once modules need to reference each
  other? `Organization` has nothing to flatten yet, so we're deferring this
  until we build a second module against a real example.

### Current Architecture

```
com.boosterhub
├── organization   (api → application → infrastructure → domain)  ◄─ first full slice
├── user           (domain: User, Role, OrganizationMembership)
├── team           (domain: Team, TeamAthlete, CoachTeamAssignment)
└── athlete        (domain: Athlete, ParentAthleteRelationship)

Organization module — first complete vertical slice:

  HTTP GET /api/organizations
           │
           ▼
  OrganizationController      (api)
           │
           ▼
  OrganizationService         (application, @Transactional readOnly)
           │
           ▼
  OrganizationRepository      (infrastructure, Spring Data JPA)
           │
           ▼
  organizations table         (PostgreSQL, schema owned by Flyway)
           │
           ▼
  Organization (entity)  ──►  OrganizationResponse (DTO)  ──►  JSON
```

### What's Next

**Sprint 2: Seed Data and the First Visible Vertical Slice.** Load
fictional organization seed data, exercise `GET /api/organizations` against
real records, and stand up the first React app with a dashboard that
displays that data — making the slice built this sprint actually visible
end to end. Identity & Authentication remains the next backend foundation
after that: no write endpoint should ship before there's a real auth story
to guard it, but it isn't the immediate next sprint.

### Sprint Metrics

| Metric             | Value |
|---------------------|-------|
| Domain Models        | 8     |
| REST Endpoints       | 1     |
| Controllers          | 1     |
| Services             | 1     |
| Repositories         | 1     |
| DTOs                 | 1     |
| Database Tables      | 8     |
| Flyway Migrations    | 2     |
| Passing Tests        | 1     |
| Frontend Screens     | 0     |
| Lines of Code (Java) | 772   |

### Sprint Outcome

Foundation established. The architecture is validated end to end, against
a real database, not mocks. Future modules should extend this pattern —
package, repository, service, controller, DTO — rather than invent a new
one.

### Confidence Level

Scores reflect what's actually been exercised so far, not aspiration — API
Design is mid-low because only one trivial GET has been tested; Authentication
is zero because nothing exists yet.

```
Architecture      [███████░░░] 7/10
Domain Model      [██████░░░░] 6/10
API Design        [█████░░░░░] 5/10
Authentication    [░░░░░░░░░░] 0/10
```

---
