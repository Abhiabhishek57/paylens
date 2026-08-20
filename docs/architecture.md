# Initial Architecture

## First vertical slice

```text
Payment Events
      |
      v
Validation + Normalization
      |
      v
Deduplication / Event Ordering
      |
      v
Pattern Detection
      |
      v
Cluster-level Diagnosis
      |
      v
Financial Impact
      |
      v
Decision Engine
  /       |       \
INTERVENE IGNORE ESCALATE
      |
      v
Audit Record
```

## Responsibility boundaries

### Deterministic backend
- Validate input fields.
- Normalize timestamps and monetary values.
- Detect duplicates.
- Maintain state transitions.
- Calculate financial values.
- Enforce guardrails.
- Persist audit records.

### AI layer
- Generate root-cause hypotheses from structured evidence.
- Compare competing explanations.
- Explain why a cluster was classified.
- Surface uncertainty for human escalation.

## Initial technology direction
- Java
- Spring Boot
- PostgreSQL
- JSON/REST
- Razorpay Test APIs/webhooks later
- LLM API later

The architecture must remain a modular monolith until a real scaling requirement is demonstrated.
