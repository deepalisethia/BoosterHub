# BoosterHub

| | |
|---|---|
| **Current Version** | v0.0.1 |
| **Sprint** | Sprint 1 |
| **Build Status** | Passing |
| **Architecture** | Modular Monolith |
| **Last Updated** | July 2026 |

BoosterHub is a platform for school athletic booster clubs that replaces
the patchwork of spreadsheets, Venmo payments, TeamSnap exports,
SignUpGenius links, email chains, and volunteer trackers with a single
system built specifically for booster organizations.

BoosterHub is an administrative operating platform for booster
organizations. It provides a central foundation for managing
organizational membership, responsibilities, teams, athletes, events,
volunteer activity, and payment tracking. It is not a coaching platform,
athlete-performance platform, recruiting platform, practice-management
system, social network, or accounting system.

This repository documents the engineering journey of building that
platform from the ground up using a modular monolith architecture: clear
module boundaries, a schema owned by migrations rather than framework
magic, and a REST API that never leaks its persistence model. Every
architectural decision made along the way is recorded, not just
implemented.

At this stage, BoosterHub is early — one domain modeled, one read-only
endpoint shipped, and a backend-only codebase. The goal of this README is to
describe accurately what exists today, not what is planned.

---

## Why This Project Exists

Most school booster clubs run on a patchwork of tools that were never
designed to work together: TeamSnap for rosters, Venmo for payments,
Google Sheets for tracking volunteers, SignUpGenius for events, and email or
group texts for everything that falls through the cracks. Each tool solves
one piece of the problem in isolation, and the coordination cost of stitching
them together falls on volunteer parents and coaches who don't have the time
for it.

BoosterHub isn't attempting to replace all of these on day one. It starts
from the data that every other tool eventually needs to reference anyway —
organizations, members, teams, and athletes — and is being built outward
from there, one real feature at a time.

The initial customer is a single booster organization — for example,
Canyon Cross Country Boosters or Canyon Boys Soccer Boosters — not a school
district or an athletic department. Multiple booster organizations already
coexist at the same school in practice, often with no shared system between
them; the architecture is intended to support that directly, rather than
assume one booster organization per school.

A few principles guide how BoosterHub approaches this problem:

- Build around real booster organizations, not idealized ones.
- BoosterHub adapts to the organization, rather than forcing the
  organization to adapt to the software.
- Favor configurable organization positions over hardcoded organizational
  structure.
- Complete user journeys matter more than isolated features.

---

## Engineering Goals

This repository prioritizes, in order:

- Clean architecture and clear module boundaries
- Modularity — features that can be understood and changed independently
- Maintainability over cleverness
- Incremental delivery — complete vertical slices over partial breadth
- Production-ready practices (migrations, transactional boundaries, DTOs)
- Documented engineering decisions, not just working code

Feature count is intentionally secondary to these goals. A small number of
well-structured features is worth more here than a large number of
half-finished ones.

---

## Current Technology Stack

Only technologies actually present in this repository are listed below.

**Backend**

| Technology | Role |
|---|---|
| Java 17 | Language and runtime |
| Spring Boot 4.1 | Application framework |
| Spring Data JPA | Persistence layer |
| Spring Web MVC | REST controllers |
| Spring Validation | Bean validation (starter present) |
| Flyway | Database schema migrations |
| PostgreSQL JDBC Driver | Database connectivity |
| Maven Wrapper | Build tool (no local Maven install required) |

**Frontend (planned)**

| Technology | Status |
|---|---|
| React | Not started — `frontend/` directory is reserved but empty |
| Node.js 22 | Pinned via `.nvmrc` for future frontend tooling |

**Infrastructure**

| Technology | Role |
|---|---|
| PostgreSQL 17 | Primary database |
| Docker Compose | Local database orchestration (`infrastructure/docker-compose.yml`) |

**Testing**

| Technology | Role |
|---|---|
| Spring Boot Test (Data JPA, Web MVC, Validation, Flyway starters) | Test infrastructure |
| JUnit (via Spring Boot Test) | Test runner |

---

## Architecture

BoosterHub is a **modular monolith**: a single deployable Spring Boot
application internally organized into feature modules with explicit
boundaries, rather than either one undifferentiated codebase or a set of
separately deployed services.

### Why a Modular Monolith?

Most applications never become microservices. Starting with a modular
monolith lets us:

- Move quickly
- Keep deployment simple
- Preserve clear boundaries
- Extract modules later if growth requires it

We are optimizing for maintainability today while leaving room for future
scale.

Packages are organized by **business capability** (`organization`, `user`,
`team`, `athlete`) rather than by technical layer (all controllers together,
all repositories together). This keeps each feature's boundary visible by
inspection and leaves the option open to extract a module into its own
service later, without first having to untangle it from unrelated code.

Each feature module is structured into four layers:

| Layer | Responsibility |
|---|---|
| `api` | REST controllers and response DTOs. The HTTP boundary — never touches repositories directly. |
| `application` | Services. Owns business logic and transactional boundaries. |
| `domain` | Core entities and relationships. Currently JPA-mapped; independent of HTTP/API concerns. |
| `infrastructure` | Spring Data repositories and other persistence-framework-specific code. |

```
com.boosterhub
├── organization   (api → application → infrastructure → domain)
├── user           (domain only)
├── team           (domain only)
└── athlete        (domain only)
```

Only `organization` currently has a complete slice through all four layers.
The other modules are modeled at the domain/schema level but not yet exposed
through the API — see [Current Domain Model](#current-domain-model) below.

Request flow through a complete slice, using the one endpoint that exists
today:

```
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

---

## Current Domain Model

The schema currently defines eight tables, modeled as eight JPA entities
across four feature packages:

| Entity | Package | Description |
|---|---|---|
| `Organization` | `organization.domain` | A booster organization (name, school, mascot, active flag). |
| `User` | `user.domain` | A person with login-worthy identity (name, unique email, active flag). |
| `OrganizationMembership` | `user.domain` | Links a `User` to an `Organization` with a role (`ADMIN`, `COACH`, `PARENT`, `ATHLETE`). |
| `Team` | `team.domain` | A team within an organization, with a category (`BOYS`, `GIRLS`, `COED`). |
| `Athlete` | `athlete.domain` | An athlete's roster record. May optionally link to a `User` — a roster entry does not require a login account to exist. |
| `TeamAthlete` | `team.domain` | Links an `Athlete` to a `Team` (roster membership). |
| `CoachTeamAssignment` | `team.domain` | Links a `User` (as coach) to a `Team`. |
| `ParentAthleteRelationship` | `athlete.domain` | Links a parent `User` to an `Athlete`, with an optional relationship type. |

Membership, rostering, coaching, and parent relationships are modeled as
distinct tables with their own foreign keys and constraints, rather than one
generic relationship table — each relationship has different cardinality
and integrity rules that the schema enforces directly. See
[`docs/ARCHITECTURE_DECISIONS.md`](docs/ARCHITECTURE_DECISIONS.md) (ADR-004)
for the full reasoning.

---

## Current Progress

- Modular monolith package structure established, organized by business
  capability.
- Domain modeled across `organization`, `user`, `team`, and `athlete`:
  8 JPA entities backed by 8 tables, created via 2 Flyway migrations.
- Flyway configured as the sole owner of schema; Hibernate runs in
  `ddl-auto=validate` and never generates or alters schema itself.
- One complete, working REST endpoint: `GET /api/organizations`, returning
  active organizations ordered alphabetically by name as
  `OrganizationResponse` DTOs.
- Full Maven test suite passes against a live PostgreSQL instance
  (currently one Spring context-load test).

Not yet implemented: authentication, any write endpoints (create, update,
delete), REST exposure for `user`, `team`, or `athlete`, and the frontend.

---

## Engineering Principles

The following are already in practice in this codebase, not aspirational:

- **Constructor injection** everywhere — no field injection.
- **DTOs instead of entities** in REST responses — JPA entities are never
  serialized directly.
- **Flyway owns the schema.** No auto-generated DDL, ever.
- **Hibernate validates, never mutates** (`ddl-auto=validate`) — mapping
  drift fails the application at startup instead of silently patching the
  database.
- **Service layer owns business logic and transactional boundaries** —
  controllers depend on services, never on repositories directly.
- **Feature-first package organization** (`api` / `application` / `domain`
  / `infrastructure` per business capability).
- **Modular monolith** — one deployable unit, internally partitioned by
  capability rather than by technical layer.

---

## Getting Started

**Prerequisites:** Java 17, Docker.

```bash
# 1. Clone the repository
git clone <repository-url>
cd BoosterHub

# 2. Start PostgreSQL
cd infrastructure
docker compose up -d

# 3. Run the application (from the backend directory)
cd ../backend
./mvnw spring-boot:run

# 4. Run the test suite
./mvnw test
```

The application connects to `jdbc:postgresql://localhost:5432/boosterhub`
(credentials defined in `infrastructure/docker-compose.yml` and
`backend/src/main/resources/application.properties`). Flyway migrations run
automatically on startup.

---

## Development Philosophy

BoosterHub is being built one **vertical slice** at a time: a single
feature taken all the way from database to HTTP response, end to end,
before moving on to the next one. Sprint 1's `organization` module — schema,
repository, service, controller, and DTO for one read-only endpoint — is the
first example of this.

The alternative — starting several features at once and building them out
in parallel layers — was deliberately avoided. It's easy to end up with
several incomplete features and no single one that actually works, and any
mistake in the underlying pattern gets multiplied across all of them before
it's caught. Proving the pattern once, completely, means every module after
it can follow the same shape with much less guesswork.

---

## Engineering Journal

Every sprint is recorded in
[`docs/ENGINEERING_JOURNAL.md`](docs/ENGINEERING_JOURNAL.md): the goal, why
it mattered, what was built, the engineering decisions made, mistakes,
lessons learned, and what changed in the architecture as a result.
Significant decisions also get a standalone, timeless entry in
[`docs/ARCHITECTURE_DECISIONS.md`](docs/ARCHITECTURE_DECISIONS.md).

The journal is chronological and cumulative — later entries build on
earlier ones and say so explicitly when an earlier decision turned out to be
wrong, rather than editing history. It's best read from Sprint 1 forward.

---

## Repository Structure

```
BoosterHub/
├── backend/
│   ├── mvnw, mvnw.cmd, pom.xml
│   └── src/
│       ├── main/
│       │   ├── java/com/boosterhub/
│       │   │   ├── BoosterHubApplication.java
│       │   │   ├── organization/
│       │   │   │   ├── api/            (OrganizationController, OrganizationResponse)
│       │   │   │   ├── application/    (OrganizationService)
│       │   │   │   ├── domain/         (Organization)
│       │   │   │   └── infrastructure/ (OrganizationRepository)
│       │   │   ├── user/domain/        (User, Role, OrganizationMembership)
│       │   │   ├── team/domain/        (Team, TeamCategory, TeamAthlete, CoachTeamAssignment)
│       │   │   └── athlete/domain/     (Athlete, ParentAthleteRelationship)
│       │   └── resources/
│       │       ├── application.properties
│       │       └── db/migration/       (V1__…, V2__…)
│       └── test/java/com/boosterhub/   (BoosterHubApplicationTests)
├── frontend/                            (reserved for future React app)
├── infrastructure/
│   └── docker-compose.yml               (PostgreSQL 17)
├── docs/
│   ├── ENGINEERING_JOURNAL.md
│   └── ARCHITECTURE_DECISIONS.md
├── .nvmrc
└── README.md
```

---

## Roadmap

Kept intentionally high level — see the Engineering Journal for what's
actually being worked on right now:

- Implement Phase 1 of ADR-008 by establishing Person while temporarily
  retaining legacy Role.
- Review and commit Phase 1 before beginning Phase 2.
- Extend the read-only vertical-slice pattern to `team` or `athlete` only
  after the domain concepts used by that slice have completed the
  applicable ADR-008 phase.
- Keep authentication ahead of protected write endpoints.
- Keep React frontend work deferred until the immediate backend
  foundation is aligned.

---

## Contributing

This is currently a solo engineering project and isn't accepting outside
contributions. The codebase is structured deliberately (clear module
boundaries, documented decisions) so that changing this in the future would
be straightforward.

---

## License

Licensed under the MIT License.
