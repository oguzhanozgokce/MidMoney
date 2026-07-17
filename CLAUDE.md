# CLAUDE.md

Guidance for Claude Code when working in the MidMoney repository.

## Skill Gate

`.claude/skills/_index.md` is the authority for skill selection.

**Apply the gate** before: creating a feature/module, adding or changing a ViewModel,
Repository, Mapper, DI module, network endpoint, navigation route, or Compose screen, and
before any refactor that crosses a layer boundary.

Steps: read `.claude/skills/_index.md` → match the task → read that `SKILL.md` → follow it.
Read `best-practices` first for any implementation task.

**Skip the gate** only for trivial, behavior-preserving edits (typos, comments, formatting).

## Build & Verify

```bash
./gradlew ktlintFormat detekt :app:assembleProdDebug test
```

Flavors: `preprod` and `prod`. Use `assembleProdDebug` / `assemblePreprodDebug` (no plain
`assembleDebug`).

## Architecture

Multi-module Clean Architecture + MVI.

- `app` — DI wiring, Navigation3 host, flavors, `AppConfig`.
- `feature:<name>` — UI + ViewModel only.
- `plugin:<name>` — business domain + data (owns repositories).
- `library:<name>` — pure infra (common, designsystem, error, event, logger, mvi, navigation,
  network, datastore, websocket).

Direction: `feature → plugin → library`. See `best-practices` for MVI, error handling
(`ErrorHandler`/`AppError`), mappers, DI, test tags, and event constants.
