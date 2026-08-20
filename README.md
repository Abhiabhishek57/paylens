# PayLens

AI-assisted payment failure diagnosis and bounded revenue intervention system.

## Problem
Payment failures are not all caused by customer intent. A merchant may lose revenue because of infrastructure or payment-rail degradation, customer-side issues, duplicate or delayed events, or ambiguous failures. Automatically retrying every failure can create unnecessary cost, friction, and operational risk.

## Core idea
PayLens analyzes payment-event batches, identifies systemic failure patterns, estimates financial impact, and produces one explicit decision for each cluster:

- **INTERVENE** — evidence supports a bounded recovery action.
- **IGNORE** — intervention is not economically or operationally justified.
- **ESCALATE** — evidence is insufficient or unsafe for automation.

> **Diagnose → Decide → Constrain → Act → Audit**

## Initial scope
1. Ingest messy synthetic payment events.
2. Validate and normalize events.
3. Detect duplicates and conflicting events.
4. Cluster related failures at pattern level.
5. Estimate revenue at risk and potential intervention cost.
6. Produce an auditable INTERVENE / IGNORE / ESCALATE verdict.
7. Test adversarial cases.
8. Add AI only where reasoning over ambiguous evidence is useful.

## Architecture direction
```text
Payment Events
      ↓
Normalization + Deduplication
      ↓
Pattern Detection / Clustering
      ↓
Root-Cause Analysis
      ↓
Financial Impact
      ↓
Decision Engine
 ┌────────────┬────────────┬────────────┐
 │ INTERVENE │   IGNORE   │  ESCALATE  │
 └────────────┴────────────┴────────────┘
      ↓
Guardrails + Action Policy
      ↓
Audit Trail
      ↓
Evaluation Metrics
```

## Engineering principles
- Deterministic code owns money calculations, validation, state transitions, deduplication, and guardrails.
- AI is used for hypothesis generation, evidence interpretation, and explanation where ambiguity exists.
- Every automated action is bounded and auditable.
- Test data includes adversarial and failure-ordering cases rather than only clean happy paths.

## Planned stack
- Java
- Spring Boot
- PostgreSQL
- Razorpay Test APIs / webhooks
- LLM API for bounded reasoning
- GitHub

## Status
**Phase 1 — Event ingestion and normalization**

The implemented vertical slice exposes `POST /api/events`. It validates a payment event, stores it in memory, and uses `eventId` as an idempotency key.

### Run

With Java 21 and Maven installed:

```bash
mvn spring-boot:run
```

### Try the endpoint

```bash
curl -i -X POST http://localhost:8080/api/events \
  -H "Content-Type: application/json" \
  -d '{"eventId":"evt_001","transactionId":"txn_001","amount":500,"currency":"INR","paymentMethod":"UPI","status":"FAILED","failureCode":"GATEWAY_TIMEOUT"}'
```

The first request returns `201 Created` with `ACCEPTED`. Repeating the same `eventId` returns `409 Conflict` with `DUPLICATE_EVENT`. Invalid fields return `400 Bad Request` with `VALIDATION_ERROR` and the affected field names.
