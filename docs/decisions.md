# Decision Log

## D001 — Diagnosis before recovery

**Decision:** Analyze failures at cluster/pattern level before choosing recovery actions.

**Why:** A retry-first system can create unnecessary retries, customer friction, and cost when the underlying problem is infrastructure or another systemic condition.

## D002 — Three-way decision

Every cluster must resolve to:

- INTERVENE
- IGNORE
- ESCALATE

**Why:** The system must be able to explicitly choose not to automate.

## D003 — Deterministic financial controls

Money calculations, duplicate detection, state transitions, idempotency, and guardrails remain deterministic.

**Why:** These controls should not depend on probabilistic model output.

## D004 — AI only where ambiguity exists

AI may generate hypotheses, interpret evidence, and explain decisions. It should not own core monetary calculations or safety rules.

## D005 — Modular monolith first

Use a single Spring Boot application initially.

**Why:** The current scale and deadline do not justify microservices complexity.
