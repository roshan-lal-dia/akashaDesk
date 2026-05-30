# AkashaDesk KMP Client

The client starts with the desktop JVM target and shared domain modules. Android and WASM app targets will be added after the shared state, storage, and navigation contracts stabilize.

## Modules

- `shared`: Compose command-center shell.
- `desktopApp`: Windows desktop launcher for the shared UI.
- `core-network`: typed Ktor HTTP client for the core backend.
- `core-database`: SQLDelight local cache schema.
- `feature-crucible`: quest and HP/XP domain model.
- `feature-obsidian`: local markdown vault bridge.
- `feature-megaphone`: markdown draft and publish preparation logic.

