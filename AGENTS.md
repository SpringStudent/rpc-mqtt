# Repository Guidelines

## Project Structure & Module Organization

```
rpc-mqtt/
├── pom.xml                     # Parent POM, dependency management
├── rpc-mqtt-common/            # Core: data structures, filters, timer
├── rpc-mqtt-client/            # Service provider — exports remote services
├── rpc-mqtt-server/            # Service consumer — invokes remote services
└── rpc-mqtt-demo/              # Usage examples
    ├── demo-server             # Consumer example (Spring Boot)
    ├── demo-client / client2   # Provider examples (Spring Boot)
    └── demo-no-spring-client   # Provider example (no Spring)
```

Each module follows Maven conventions: `src/main/java/io/github/springstudent/` for source code. No tests exist yet; test submissions should go under `src/test/java` following the same package structure.

## Build, Test, and Development Commands

| Command | Purpose |
|---|---|
| `mvn clean install` | Build all modules from root |
| `mvn clean install -pl rpc-mqtt-client,rpc-mqtt-server -am` | Build only core modules (skip demos) |

The project targets **Java 8**. No test runner is currently configured. Run demo modules individually via your IDE or the Spring Boot Maven plugin.

## Coding Style & Conventions

- **Java 8** language level.
- **Naming**: standard Java conventions — PascalCase for classes, camelCase for methods and fields.
- **Package**: `io.github.springstudent`, subpackaged by module role (`common`, `client`, `server`).
- **Serialization**: all RPC messages use JSON via **Gson** (the only cross-module dependency).
- **Javadoc**: `@author` on public classes; keep inline comments short and only where code is not self-explanatory.
- **Filters**: implement `RpcMqttFilter extends Orderable`; order values should use distinct integers or clearly separated ranges (`MIN_VALUE` / `0` / `MAX_VALUE`). Do not mutate the chain filter list after construction.
- **No external formatter or linter** is configured — match the style of surrounding code.

## Testing Guidelines

No test framework is included yet. When adding tests:

- Add JUnit to the relevant module pom.xml.
- Place tests under `src/test/java/io/github/springstudent/`.
- Name test classes `*Test.java`.
- Cover at minimum: `RpcMqttChain` traversal, `RpcRemoteOnlineManager` heartbeat timeout, payload serialization round-trip, and filter chain execution order.

## Commit & Pull Request Guidelines

- Write messages in Chinese or English, but be consistent within a single commit.
- Keep each commit scoped to one logical change.
- Before opening a PR, verify `mvn clean install` passes for the modules you changed.
- Link any related issue in the PR description.
- If the PR changes behaviour visible to consumers (request/response fields, topic naming, timeout semantics), describe the migration path.

## Agent-Specific Instructions

This file doubles as the agent contract for Codex agents working on this repository. When modifying code:

- Fix bugs before refactoring. Do not rename symbols or restructure code while fixing a bug.
- Add no abstraction for single-use code. Add no feature beyond what is requested.
- When the cure is worse than the disease, say so and propose the simpler path.
- Use `mvn compile -pl <module>` for quick compilation checks.
- Prefer `rg` for search; use `apply_patch` for manual edits.
- Record non-obvious project-specific gotchas in `tasks/lessons.md` after each correction.
- Reply in Chinese by default; switch to English when the discussion touches code identifiers or API design names authored in English.
