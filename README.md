# AkashaDesk

AkashaDesk is a sovereign command-center workspace for Dia and authenticated companions. The implementation follows the D.I.A. stack from the Master Requirements Document:

- Kotlin Multiplatform Compose client modules under `apps/kmp-client`
- Kotlin Ktor core backend under `services/core-engine`
- Bun and BetterAuth identity service under `services/auth-identity`
- Python FastAPI AI microservice under `services/ai-agent`
- Shared PostgreSQL schema definitions under `shared/schema`
- Cloudflare Tunnel as the only public edge path

## Current Implementation Status

The app is currently focused on Crucible first. The desktop KMP app now boots into an interactive Crucible dashboard backed by a pure Kotlin domain engine for Endurance, Grind, and Sprint quests.

No backend service publishes a host port. Postgres stays on the internal Docker network only, while auth and AI services keep outbound egress for OAuth provider calls and GitHub publishing.

## Run The Desktop App

```powershell
mise --yes exec -- gradle :apps:kmp-client:desktopApp:run
```

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

The active product slice is Crucible:

1. Create Endurance, Grind, and Sprint quests from the command panel.
2. Select quests from the quest index.
3. Mark active quests complete or failed.
4. Watch HP, XP, level, streak, quest status, and event log update immediately.

Megaphone/AI remains scaffolded for the later blog-maker slice:

1. KMP client prepares a markdown draft.
2. Core backend receives `POST /v1/publish` with a bearer token.
3. Core backend forwards the typed payload to the internal AI service.
4. AI service sanitizes the draft and returns SEO metadata plus a target path.

GitHub commits are represented by the contract and environment variables, but the real GitHub API adapter is intentionally left for the next slice.
