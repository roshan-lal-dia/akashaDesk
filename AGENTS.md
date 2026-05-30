# AkashaDesk Agent Rules

These rules apply to the entire repository unless a more specific `AGENTS.md` exists in a subdirectory.

## Project Context

- Build AkashaDesk as a decoupled, multi-language monorepo for a sovereign desktop/mobile/web command center.
- Preserve the D.I.A. stack boundaries from the Master Requirements Document:
  - `apps/kmp-client`: Kotlin Multiplatform Compose UI and shared client logic.
  - `services/core-engine`: Kotlin JVM Ktor backend.
  - `services/auth-identity`: Bun BetterAuth identity service.
  - `services/ai-agent`: Python AI microservice.
  - `shared/schema`: shared PostgreSQL schema definitions.
- Target deployment is 1-click Docker Compose plus Cloudflare Zero Trust tunnels.

## Architecture Directives

- Keep the monorepo structure strict. Do not move cross-service contracts into ad hoc locations.
- Maintain Clean Architecture in Kotlin modules. UI code must talk through interactors/use cases, not directly to databases.
- Enforce strong type safety across Kotlin code. Prefer sealed classes, explicit domain models, and typed API contracts.
- Shared KMP modules must only use libraries compatible with Compose Multiplatform/KMP. Avoid JVM-only dependencies in shared code.
- The Obsidian Bridge is local-only markdown file access. Do not add cloud database synchronization to that module.
- The Megaphone publish flow is KMP app -> core backend -> internal Python AI service -> GitHub API commit.

## Infrastructure Directives

- Backend services routed by Cloudflare must not publish public host ports in `docker-compose.yml`.
- Use Docker internal networking for service-to-service communication.
- Define health checks and strict dependency ordering for containers, especially services depending on Postgres.
- Use BetterAuth for identity. Do not implement custom cryptography or bespoke OAuth/session handling.

## Design Directives

- Follow `archive/project-support/DESIGN.md` for the Sovereign Terminal visual system.
- Favor dense, utilitarian command-center UI over marketing-style layouts.
- Use rectilinear components, 1px modular dividers, sparse neon signal colors, and terminal-first typography.
- The user also likes Duolingo and Strava-style motivation loops; apply that preference mainly to Crucible gamification feedback, not to the whole visual system.

## Change Ledger Requirement

- Every workspace modification must be recorded in `KEEP_CHANGES.MD`.
- Append a new entry under `## Entries` for each meaningful change before finishing work.
- Use the exact template already defined in `KEEP_CHANGES.MD`:
  - Timestamp (UTC)
  - Summary
  - Files Changed
  - Checks Run
  - Result
- Include commands run for verification. If no checks were run, explicitly write `Not run`.
- Do not delete or rewrite previous ledger entries unless the user explicitly asks.

## Agent Working Rules

- Read the Master Requirements Document before making architectural decisions.
- Keep edits scoped to the requested change and existing project direction.
- Do not overwrite user changes. If the worktree is dirty, inspect relevant files and work with the current state.
- Prefer `rg`/`rg --files` for repository discovery.
- Update documentation when decisions affect architecture, deployment, or agent behavior.
