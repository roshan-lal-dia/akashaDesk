# AkashaDesk: Master Requirements Document (v1.2)
**Project Name:** AkashaDesk
**Target Audience:** Single sovereign user (Dia) + authenticated multiplayer companions.
**Deployment Strategy:** 1-Click Docker Compose (Local/Server) + Native KMP binaries + Cloudflare Zero Trust Tunnels.

## 1. Architectural Blueprint: The D.I.A. Stack
This project strictly adheres to a decoupled, multi-language monorepo architecture. 

### 1.1. Core Tech Stack
* **Edge Proxy/Tunnel:** Cloudflare Tunnels (`cloudflared`). Zero exposed public ports.
* **Client (UI):** Kotlin Multiplatform (KMP) with Compose Multiplatform.
    * Targets: `desktopApp` (Windows JVM with System Tray), `androidApp` (Mobile), `wasmJsApp` (Web fallback).
* **Core Backend (Logic):** Kotlin JVM with Ktor.
* **Identity Service:** Bun runtime executing BetterAuth.
* **AI Microservice:** Python (FastAPI/Flask) for LLM orchestration and GitHub API manipulation.
* **Databases:**
    * *Central Vault:* PostgreSQL (Containerized).
    * *Local/Offline Cache:* SQLite (via SQLDelight in KMP).

### 1.2. Monorepo Structure & Justification
The monorepo structure is non-negotiable. It ensures unified CI/CD and prevents API contract drift between the Kotlin Engine and Python AI Agent.

```text
akashadesk/
├── docker-compose.yml          # The 1-click deployment orchestrator (Includes cloudflared)
├── .mise.toml                  # Strict toolchain versioning (Bun, Kotlin, Python)
├── apps/
│   ├── kmp-client/             # Compose Multiplatform UI & Shared Logic
│       ├── core-database/      # SQLDelight SQLite offline cache
│       ├── core-network/       # Ktor HTTP client
│       ├── feature-crucible/   # Gamification logic
│       ├── feature-obsidian/   # Local file system reader
│       └── feature-megaphone/  # Markdown editor UI
├── services/
│   ├── auth-identity/          # Bun + BetterAuth (Google/GitHub OAuth)
│   ├── core-engine/            # Kotlin Ktor Backend
│   └── ai-agent/               # Python Microservice
└── shared/
    └── schema/                 # Shared PostgreSQL schema definitions (Drizzle)
2. Infrastructure: 1-Click Docker Compose with Cloudflare Tunnels
The system must be fully deployable via a single docker-compose up -d command.
CRITICAL: No ports will be published to the host machine for backend services.
The docker-compose.yml must provision the following interconnected containers:

cloudflared-tunnel: Connects the internal Docker network to the Cloudflare Edge. Routes traffic from auth.yourdomain.com directly to the auth-service container, and api.yourdomain.com to core-backend.

postgres-db: The central source of truth. Fully isolated on the internal network.

auth-service: The Bun/BetterAuth container. Connects to postgres-db.

core-backend: The Kotlin Ktor server. Connects to postgres-db and validates BetterAuth tokens.

ai-service: The Python microservice. Exposes an internal API strictly for the core-backend to trigger blog deployments.

3. The Authentication Flow (Hybrid OAuth)
Custom cryptography is forbidden. We utilize BetterAuth for identity management.

The KMP App triggers "Login with GitHub/Google" by opening the system browser, pointing to auth.yourdomain.com.

Upon successful OAuth validation, BetterAuth redirects via a custom OS deep link (e.g., akashadesk://auth-callback?token=XYZ).

The KMP App intercepts the deep link, stores the token in native secure storage (EncryptedSharedPreferences / Credential Locker), and attaches it as a Bearer token for all future core-backend Ktor requests.

4. Feature Specifications
4.1. The Crucible (Multiplayer Gamification Engine)
A rigorous habit and quest tracker utilizing an RPG engine (HP/XP).

Data Sync Logic: Offline-first. Reads/writes to local SQLite via SQLDelight. Background syncs with PostgreSQL via core-backend when a network connection is detected to update multiplayer state.

Polymorphic Quest Schema (Sealed Classes):

Endurance: E.g., "No Sugar". No XP gained for existing; massive HP damage if failed.

Grind: E.g., "Exercise 30 mins". Steady XP rewards for daily completion.

Sprint: E.g., "7-Day Code Challenge". Time-boxed with a massive completion bounty.

Multiplayer Arena: Users can generate invite links (authenticated via BetterAuth) to challenge companions to Sprint or Endurance quests. Central Postgres database tallies group HP/XP and broadcasts failures to the group.

4.2. The Obsidian Bridge
A zero-friction markdown capture module.

Mechanism: Uses native Kotlin file system APIs (java.io.File on Desktop) to read and write raw .md files directly to a designated local directory.

Purpose: Acts as a lightning-fast capture tool that shares a sync folder with Obsidian and Google Drive.

Constraint: Operates purely locally. No cloud database synchronization required for this module.

4.3. The Megaphone (Blogging Engine)
A specialized markdown command center for authoring and deploying blog posts.

UI Requirement (Desktop): A Dual-Pane Compose Multiplatform editor. Left pane: Raw text input with regex-based visual syntax highlighting. Right pane: Instantaneous Markdown render preview.

Image Handling: Native drag-and-drop support for local images, generating base64/CDN markdown tags.

The Deployment Pipeline:

User clicks "Publish".

KMP App sends the Markdown payload to api.yourdomain.com/publish (core-backend).

core-backend routes it to the internal ai-service (Python).

Python agent sanitizes text, generates SEO metadata, and utilizes the GitHub API to commit the file to the user's Astro repository, triggering GitHub Actions.

5. Coding Directives for the AI Agent
Enforce absolute strict type safety across all Kotlin modules.

Implement Clean Architecture principles: UI cannot talk to the Database directly; it must route through Interactors/Use Cases.

If a package or library is not compatible with Compose Multiplatform or KMP, find an alternative. Do not rely on JVM-only libraries for shared modules.

Ensure all Docker containers define health checks and strict dependency startup orders (e.g., core-backend must wait for postgres-db to be healthy).

DevOps Directive: Do not map public ports in docker-compose.yml for services routed via cloudflared. Use Docker's internal networking.