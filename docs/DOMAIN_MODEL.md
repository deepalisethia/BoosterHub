# Domain Model

## Purpose

The Domain Model defines the core business concepts that make up BoosterHub.

Each concept represents a real-world entity, responsibility, or relationship within a booster organization and is described independently of implementation, user interface, or database design.

This document establishes the shared language used throughout the product and serves as the foundation for product design, engineering, and implementation.

---

## Core Domain Principles

- Model the business, not the software.
- Model people by who they are, not by the activities they perform.
- Separate identity, responsibility, and permission.
- Every domain concept has a single, well-defined responsibility.
- Relationships between concepts should reflect the real world.
- Avoid duplicate or overlapping concepts.
- Favor explicit domain concepts over implicit behavior.

---

## Primary Domain Concepts

### Organization

**Purpose**

Represents one booster organization operating within a school community. The Organization is BoosterHub's primary customer and the boundary within which people, responsibilities, permissions, teams, and operational activity are managed.

**Responsibilities**

- Maintain the organization's identity and operating status.
- Provide the boundary for memberships, positions, teams, and organizational activity.
- Preserve organizational information independently of leadership changes.

**Relationships**

- Is associated with a school.
- Has many Memberships.
- Defines built-in and custom Positions.
- Has many Teams.
- Will eventually contain Seasons, Events, Payments, and Volunteer Activities.

**Notes**

- A school may be associated with multiple independent booster organizations.
- Each organization manages its own members, positions, permissions, and operational information.
- A school is contextual information; BoosterHub does not administer the school.
- BoosterHub initially serves school-based booster organizations. Supporting other volunteer-led organizations is a future opportunity, not a current design requirement.

---

### Person

**Purpose**

Represents a real human known to BoosterHub, independently of any organization, responsibility, activity, or login account.

**Responsibilities**

- Maintain the person's core identity and contact information.
- Allow the same person to participate in multiple organizations.
- Provide the human identity used by Memberships and explicit domain relationships.

**Relationships**

- May have Memberships in multiple Organizations.
- May have one global login account.
- May have zero or more organization-specific Athlete records through Memberships.
- May participate in Parent–Athlete or Coach–Team relationships.

**Notes**

- Login is required to access BoosterHub.
- An Organization may create a Person record before the Person creates a login account, such as when preparing a roster or invitation.
- Every Athlete record is associated with exactly one Person through its Membership.
- Parent, Athlete, and Coach are contextual identities, not permanent Person attributes.
- Organizational status belongs to Membership.
- Positions and Permissions are not assigned directly to Person.
- Other organizations cannot see a Person's memberships or activity outside their own organization.

---

### Membership

**Purpose**

Connects one Person to one Organization and establishes their participation within that organization.

**Responsibilities**

- Track membership status.
- Support invitation, approval, activation, and deactivation.
- Provide the organization-specific boundary for positions and access.
- Connect the Person to organization-specific relationships and activity.

**Relationships**

- Belongs to one Person.
- Belongs to one Organization.
- May have one or more Position assignments.
- May have one Athlete record.
- May participate through Parent–Athlete or Coach–Team relationships when applicable.

**Notes**

- A Person has at most one Membership in each Organization.
- An active Membership is required to access private organization information.
- Membership does not describe whether someone is a parent, athlete, or coach.
- Membership does not represent leadership authority.
- Members without a named Position are displayed as Member.
- Member is a display label, not a Position.
- Invited members are approved by the invitation.
- Self-requested memberships require approval before private access is granted.

---

### Position

**Purpose**

Represents a named responsibility held by a member within an Organization.

**Responsibilities**

- Describe organizational responsibility.
- Group the Permissions required for that responsibility.
- Allow responsibility to be assigned, delegated, and transferred.
- Preserve leadership structure independently of the people currently serving.

**Relationships**

- Belongs to one Organization.
- Is assigned to one or more Memberships.
- Contains one or more BoosterHub-defined Permissions.
- May be built-in or created by the Organization.

**Notes**

- Built-in Positions may include President, Vice President, Treasurer, Secretary, Board Member, Coach, Volunteer Coordinator, and Organization Administrator.
- Organizations may create custom Positions.
- A Membership may hold multiple Positions.
- A Position may be held by multiple members when appropriate.
- Position assignments may change without changing the Person or Membership.
- Parent, Athlete, and Coach relationships are not created by assigning a Position.
- Member is not a Position.
- Positions do not form a ranking hierarchy; authority comes from assigned Permissions.

---

### Permission

**Purpose**

Represents a BoosterHub-defined capability to view information or perform an action within an Organization.

**Responsibilities**

- Define the actions BoosterHub can authorize.
- Protect organizational information and administrative operations.
- Allow authority to be assigned consistently through Positions.
- Support delegated responsibility without granting unnecessary access.

**Relationships**

- Is defined and maintained by BoosterHub.
- Is assigned to one or more Positions.
- Becomes effective when a member holds the associated Position through an active Membership.
- May be limited by explicit relationships, such as Parent–Athlete.

**Notes**

- Organizations may assign available Permissions to Positions but cannot create new Permission types.
- Permissions are never assigned directly to a Person or Membership.
- Members receive limited baseline access through active Membership.
- Parent–Athlete relationships may allow access only to linked athlete information.
- Named Positions provide additional organizational authority.
- Sensitive capabilities remain separate.
- Permission names describe actions, such as INVITE_MEMBERS, APPROVE_MEMBERS, ASSIGN_POSITIONS, MANAGE_POSITION_PERMISSIONS, and MANAGE_ROSTER.
- An authorized member assigns Positions to other members; Positions grant Permissions.
- The complete Permission catalog will be defined from approved user journeys before authorization is implemented.

---

### Team

**Purpose**

Represents an athletic team organized and supported by a Booster Organization.

**Responsibilities**

- Identify a team within the Organization.
- Organize athlete participation.
- Support coach assignments.
- Maintain the team's active status and history.

**Relationships**

- Belongs to one Organization.
- Has Athlete assignments.
- Has Coach–Team assignments.
- May participate in future Seasons and Events.

**Notes**

- An Organization may support multiple Teams.
- Team names and categories are defined within the Organization.
- A Person participates through organization-specific Athlete or Coach relationships.
- Team participation does not grant organizational authority.
- Varsity, JV, and Frosh/Soph are not permanent Athlete or Team classifications.
- Competition division belongs to a future event-participation record.
- Team excludes practice planning, coaching strategy, and athlete performance tracking.

---

### Athlete

**Purpose**

Represents a Person's athletic participation within one Booster Organization.

**Responsibilities**

- Maintain organization-specific athlete roster information.
- Track whether the athlete is currently active within the Organization.
- Support Team assignments.
- Preserve the athlete's participation history within the Organization.
- Provide the organization-specific athlete record used by Parent–Athlete relationships and future event participation.

**Relationships**

- Belongs to exactly one Membership.
- Is therefore associated with exactly one Person and one Organization through that Membership.
- May be assigned to one or more Teams over time.
- May have one or more Parent–Athlete relationships.
- May participate in future Events through event-participation records.

**Notes**

- An Athlete record cannot exist without a Membership.
- A Person may have Athlete records in multiple Organizations through separate Memberships.
- An Athlete record does not require the Person to have an active login account.
- Athlete is a contextual identity, not a permanent attribute of Person.
- Each Organization can access only its own Athlete record and related information.
- Varsity, JV, and Frosh/Soph are not permanent Athlete classifications.
- Competition division belongs to a future event-participation record.
- Athlete does not include performance tracking, coaching evaluation, or recruiting information.

---

### Season

**Purpose**

**Responsibilities**

**Relationships**

**Notes**

---

### Event

**Purpose**

**Responsibilities**

**Relationships**

**Notes**

---

### Payment

**Purpose**

**Responsibilities**

**Relationships**

**Notes**

---

### Volunteer Activity

**Purpose**

**Responsibilities**

**Relationships**

**Notes**

---

## Domain Relationships

- Person connects to Organization through Membership.
- An Athlete record belongs to Membership and therefore receives its Person and Organization context through Membership.
- Positions are assigned through Membership.
- Permissions attach to Positions.
- Parent–Athlete and Coach–Team relationships express contextual participation.
- Effective access combines active Membership, explicit relationships, and Position Permissions.
- All access remains scoped to one Organization.

---

## Modeling Guidelines

- Define the business before defining the software.
- Every domain concept should have one clear responsibility.
- Model concepts independently of implementation.
- Prefer relationships over duplicated information.
- Use terminology that reflects how booster organizations naturally describe their operations.
- New concepts should only be introduced when an existing concept cannot accurately represent the business.
