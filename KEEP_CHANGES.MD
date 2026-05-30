# Keep Changes Log

Use this file as the persistent ledger for all workspace modifications.

## Log Entry Template

- Timestamp (UTC):
- Summary:
- Files Changed:
  -
- Checks Run:
  -
- Result:

## Entries

- Timestamp (UTC): 2026-05-24T10:22:00Z
- Summary: Fixed production 500 Internal Server Error on blocked prompts. Two root causes: (1) When guardrails block a prompt, the endpoints raised `HTTPException(status_code=400)` which returns `{"detail": "..."}`, but the frontend expects an `ActionResult` JSON shape — the format mismatch caused the frontend to display a generic 500-style error. Fix: replaced `raise HTTPException(400)` with `return ActionResult(action_id=..., answer=BLOCKED_RESPONSE)` so blocked messages appear cleanly in the chat bubble. (2) All `guardrails.log()` calls in `routes.py` and `plugin_registry.py` were unprotected — if the log backend (Postgres or in-memory) throws, the exception bubbles up as an unhandled 500. Fix: wrapped every `guardrails.log()` in try/except across both files.
- Files Changed:
  - src/ask_bayan/api/routes.py
  - src/ask_bayan/core/plugin_registry.py
- Checks Run:
  - .venv\Scripts\python.exe -m pytest tests\test_prompt_injection.py tests\test_false_positive_stress.py -v -> 188 passed, 0 failed
- Result: Blocked prompts now return a proper ActionResult with the BLOCKED_RESPONSE message (HTTP 200 with clean JSON), so the frontend shows the message in the chat bubble. All guardrails.log() calls are crash-safe. 188 unit tests pass with zero regressions.

- Timestamp (UTC): 2026-05-30T12:11:27Z
- Summary: Read the AkashaDesk Master Requirements Document and added root-level agent rules to preserve the D.I.A. stack architecture, infrastructure constraints, design direction, and mandatory change-ledger workflow. Created the persistent `KEEP_CHANGES.MD` ledger using the requested format and preserved the provided initial entry.
- Files Changed:
  - AGENTS.md
  - KEEP_CHANGES.MD
- Checks Run:
  - Not run; documentation-only change.
- Result: Future agents now have repository-wide guidance for AkashaDesk and must maintain `KEEP_CHANGES.MD` for all workspace modifications.
