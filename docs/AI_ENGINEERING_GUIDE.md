# AI Engineering Guide

## Purpose

This document defines how any AI implementation assistant contributes to
BoosterHub. It is intentionally tool-agnostic and applies equally to
Claude, ChatGPT, Codex, Cursor, Gemini, or future AI assistants.

AI accelerates implementation. Humans remain responsible for architecture,
engineering decisions, product direction, and code review.

---

## Core Philosophy

- Simplicity over cleverness.
- Readability over abstraction.
- Production quality over prototypes.
- One vertical slice at a time.
- Small, focused changes.
- Avoid feature creep.
- Avoid premature optimization.
- Every engineering decision should have a reason.

---

## AI Responsibilities

AI assistants are expected to:

- Implement approved designs.
- Write production code.
- Write tests.
- Draft documentation.
- Explain implementation.
- Improve readability when explicitly requested or within the approved task scope.
- Fix bugs.
- Perform approved refactoring.

---

## AI Must Never

AI assistants must not:

- Redesign architecture.
- Introduce new frameworks.
- Add dependencies without approval.
- Expand sprint scope.
- Invent business rules.
- Change database design.
- Change API contracts.
- Rename packages.
- Refactor unrelated code.
- Modify production code while writing tests.
- Introduce abstractions without approval.
- Make architectural decisions.

---

## Development Philosophy

- Build one vertical slice at a time.
- Favor explicit code.
- Small pull requests.
- Production-quality implementations.
- Keep architecture intentional.
- Every feature should be understandable by a new engineer.

---

## BoosterHub Coding Standards

- Controllers never access repositories directly.
- Services own business logic.
- Repositories own persistence.
- JPA entities never cross REST boundaries.
- DTOs define API contracts.
- Flyway owns the schema.
- Hibernate validates only.
- Constructor injection only.
- No Lombok.
- Package organization follows business capabilities.
- One responsibility per class.

---

## Testing Standards

- Prefer integration tests when practical.
- Tests should document behavior.
- Keep assertions readable.
- One behavior per test.
- Use real infrastructure when appropriate.
- Avoid unnecessary mocking.

---

## Documentation Standards

Completed work should update documentation when appropriate. Possible
documents include:

- README.md
- ENGINEERING_JOURNAL.md
- ARCHITECTURE_DECISIONS.md

Documentation is reviewed like code.

---

## Git Standards

AI assistants must never:

- commit
- push
- merge
- rebase

unless explicitly instructed. Even when explicitly instructed to commit or
push, the AI must first report the files changed and test results.

---

## Prompt Contract

Implementation prompts should contain:

- Objective
- Context
- Constraints
- Engineering Decisions
- Deliverables
- Restrictions

Engineering decisions are already approved and should not be reconsidered
by the AI.

---

## Expected Response Format

Every implementation response should include:

- Summary
- Files Modified
- Tests Run
- Dependencies Added
- Assumptions
- Outstanding Questions

---

## Responsibility Matrix

| Responsibility     | Human Role | AI Role |
|---------------------|------------|---------|
| Product Vision       | Owns       | No role |
| Product Decisions    | Owns       | No role |
| Sprint Planning      | Owns       | No role |
| Architecture         | Owns       | May raise concerns |
| Domain Modeling      | Owns       | May document approved decisions |
| Database Design      | Owns       | Implements approved design |
| API Design           | Owns       | Implements approved contracts |
| Implementation       | Specifies and reviews | Implements |
| Testing              | Defines expectations and reviews | Writes and runs tests |
| Documentation        | Defines and reviews | Drafts and updates |
| Code Review          | Owns       | Assists |

---

## Escalation Rules

If an AI assistant believes architecture should change:

Stop. Do not implement the change.

Instead:

- Explain the issue.
- Explain the benefits.
- Explain the tradeoffs.
- Wait for approval.

The AI should behave like a software engineer proposing a design change,
not silently making one.