# Roadmap

## Completed

* Established the BoosterHub product vision.
* Defined the product decision framework.
* Established the domain-modeling foundation.
* Defined the initial organizational user journeys.
* Implemented the first backend vertical slice for organizations.
* Verified PostgreSQL, Flyway, JPA validation, DTO boundaries, and integration testing.
* Defined Organization, Person, Membership, Position, Permission, Team, and
  Athlete.
* Approved the staged transition from User and Role to the foundational
  domain model.

## Human-Owned Decisions

The following decisions must be made by a human Staff Engineer or Product
Architect before AI-assisted implementation proceeds on the related work
below. These are not mechanical implementation tasks.

* Permission catalog and assignment rules — which permissions exist, and
  how they attach to positions.
* The Payment and accounting boundary.
* Authentication and authorization design.

## In Progress

* Prepare Phase 1 of ADR-008: establish Person while temporarily retaining
  legacy Role.
* Keep the backend buildable and schema validation aligned throughout the
  transition.

## Next

* Implement and verify ADR-008 Phase 1.
* Implement Phase 2 only after Phase 1 is reviewed and committed.
* Implement the next read-only backend vertical slice only after the
  domain concepts it uses have completed the applicable ADR-008 phase.
* Define the initial Permission catalog from approved user journeys before
  Phase 3.
* Implement Position and Permission only after the catalog is approved.

Implementation tasks, independent of the domain-alignment sequence above:

* Remove the unused Lombok dependency.
* Establish a consistent API error-response convention.
* Add local development CORS configuration before frontend integration.

## Future

* Authentication and authorization.
* Organization setup and configuration.
* Membership and roster management.
* Payment tracking.
* Volunteer opportunity and participation tracking.
* Event management.
* Leadership transition workflows.
* React frontend development.

## Deferred Domain Concepts

The following concepts already exist as headings in `DOMAIN_MODEL.md` but
are intentionally left undefined until their corresponding implementation
phase below is reached. They are not omissions.

* Season — deferred to Organization setup and configuration.
* Event — deferred to Event management.
* Payment — deferred to Payment tracking.
* Volunteer Activity — deferred to Volunteer opportunity and participation
  tracking.

## Parking Lot

Ideas listed here are not approved for implementation. Each must pass the Product Decision Framework before being moved into the roadmap.

* Fundraising management.
* Sponsorship management.
* Bulk communication.
* Document and resource storage.
* Third-party integrations.
* Multi-organization reporting.
