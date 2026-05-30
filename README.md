# AkashaDesk

AkashaDesk is a sovereign command-center workspace for Dia and authenticated companions. The implementation follows the D.I.A. stack from the Master Requirements Document:

- Kotlin Multiplatform Compose client modules under `apps/kmp-client`
- Kotlin Ktor core backend under `services/core-engine`
- Bun and BetterAuth identity service under `services/auth-identity`
- Python FastAPI AI microservice under `services/ai-agent`
- Shared PostgreSQL schema definitions under `shared/schema`
- Cloudflare Tunnel as the only public edge path

## Current Implementation Status

This first implementation pass lays down the monorepo structure, Docker Compose deployment shape, health checks, and a typed Megaphone publish contract from the core backend to the AI service.

No backend service publishes a host port. Postgres stays on the internal Docker network only, while auth and AI services keep outbound egress for OAuth provider calls and GitHub publishing.

## Local Start

1. Copy `.env.example` to `.env`.
2. Fill in Cloudflare, OAuth, and token values.
3. Run:

```powershell
docker compose up -d
```

The Cloudflare tunnel should route public traffic to:

- `auth.yourdomain.com` -> `auth-service:3000`
- `api.yourdomain.com` -> `core-backend:8080`

Those routes are configured in Cloudflare for the named tunnel token.

## First Feature Slice

The initial end-to-end slice is Megaphone publishing:

1. KMP client prepares a markdown draft.
2. Core backend receives `POST /v1/publish` with a bearer token.
3. Core backend forwards the typed payload to the internal AI service.
4. AI service sanitizes the draft and returns SEO metadata plus a target path.

GitHub commits are represented by the contract and environment variables, but the real GitHub API adapter is intentionally left for the next slice.
