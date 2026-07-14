# Engineering Playbook

## Purpose

This document describes how BoosterHub is engineered. It is the team's
operating handbook: how work is performed, reviewed, documented, and
delivered.

It is not a product document. It is not an architecture decision record. It
is not a coding standards document. Those live elsewhere; this document is
the process that surrounds them.

---

## Engineering Philosophy

- Simplicity over cleverness.
- Readability over abstraction.
- Production quality over quick prototypes.
- Small vertical slices.
- Continuous improvement.
- Architecture evolves intentionally.
- Avoid premature optimization.
- Avoid feature creep.
- Every engineering decision should have a reason.

---

## Team Roles

### Product Owner

Owns:

- Product vision
- Customer needs
- Priorities

### Staff Engineer

Owns:

- Architecture
- Domain modeling
- Technical decisions
- Sprint planning
- Code review
- Acceptance criteria

### Implementation Engineer (AI)

Owns:

- Writing implementation code
- Tests
- Documentation
- Small approved refactors

Does not own architecture or product decisions.

---

## Sprint Workflow

Sprint Planning
↓
Architecture Discussion
↓
Implementation Specification
↓
Implementation
↓
Engineering Review
↓
Testing
↓
Documentation
↓
Commit
↓
Push

---

## Development Workflow

- Build one vertical slice at a time.
- Keep pull requests small.
- Controllers never contain business logic.
- Services own business logic.
- Repositories own persistence.
- DTOs cross API boundaries.
- Flyway owns schema.
- Constructor injection only.
- No Lombok.
- Favor explicit code.

---

## Definition of Done

A feature is complete only when:

- Implementation is finished.
- Tests pass.
- Documentation is updated.
- Code review is completed.
- The repository remains buildable.
- No unrelated code was modified.

---

## Git Workflow

git status
↓
git add
↓
git diff --cached
↓
Review
↓
Commit
↓
Push

Commits should be small and focused.

---

## Documentation Workflow

Documentation is treated like code. Every important engineering decision
should be documented. Documentation is reviewed before merging.

---

## Code Review Checklist

- Architecture respected?
- No unnecessary abstraction?
- Tests included?
- Documentation updated?
- No feature creep?
- Readability maintained?

---

## Refactoring Guidelines

Refactoring should occur only when it improves clarity or maintainability.
Avoid speculative refactoring.

---

## AI Collaboration Workflow

The human engineering team owns:

- Product decisions
- Architecture
- Domain modeling
- Database design
- API contracts

The AI assistant owns:

- Implementation
- Tests
- Documentation

The AI assistant must not:

- Redesign architecture.
- Introduce dependencies without approval.
- Refactor unrelated code.
- Expand sprint scope.
- Rename packages without approval.
- Add new frameworks.

---

## Repository Standards

- Consistent package organization.
- Modular monolith.
- One responsibility per class.
- Clear naming.
- Small commits.
- Production-quality code.