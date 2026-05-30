# AkashaDesk KMP Client

The client starts with the desktop JVM target and shared domain modules. Crucible is the first working surface. Android and WASM app targets will be added after the shared state, storage, and navigation contracts stabilize.

## Run

```powershell
mise --yes exec -- gradle :apps:kmp-client:desktopApp:run
```

## Modules

- `shared`: Compose command-center shell with the current Crucible dashboard.
- `desktopApp`: Windows desktop launcher for the shared UI.
- `core-network`: typed Ktor HTTP client for the core backend.
- `core-database`: SQLDelight local cache schema.
- `feature-crucible`: quest, HP/XP, streak, status, and action engine.
- `feature-obsidian`: local markdown vault bridge.
- `feature-megaphone`: markdown draft and publish preparation logic.
