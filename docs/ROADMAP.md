# Roadmap

## Completed

* Established the BoosterHub product vision.
* Defined the product decision framework.
* Established the domain-modeling foundation.
* Defined the initial organizational user journeys.
* Implemented the first backend vertical slice for organizations.
* Verified PostgreSQL, Flyway, JPA validation, DTO boundaries, and integration testing.

## Human-Owned Decisions

The following decisions must be made by a human Staff Engineer or Product
Architect before AI-assisted implementation proceeds on the related work
below. These are not mechanical implementation tasks.

* Definition of Identity, Position, and Permission in the Domain Model.
* Transition plan away from the current flat Role enum.
* Permission catalog and assignment rules — which permissions exist, and
  how they attach to positions.
* The Payment and accounting boundary.
* Authentication and authorization design.

## In Progress

* Define the foundational domain concepts required for implementation.
* Prepare the backend for the next vertical slice. This is mechanical
  groundwork only and does not include Identity, Position, or Permission
  alignment.

## Next

Domain definition must be completed before backend alignment begins:

* Define Organization, Person, Membership, Position, Permission, Team, and
  Athlete in the Domain Model.
* Define the relationship between Identity, Position, and Permission.
* Decide how the current flat Role model will transition to Identity,
  Position, and Permissions, once the Domain Model above is complete.

Only after the domain definition above is complete:

* Align the existing backend with the Identity, Position, and Permission
  model.

Implementation tasks, independent of the domain definition above:

* Remove the unused Lombok dependency.
* Implement the next read-only backend vertical slice.
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
