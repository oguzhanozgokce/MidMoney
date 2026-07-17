# MidMoney — Skill Graph

> **How to use**: Match the task to a skill, then read only that skill's `SKILL.md`.
> Read `best-practices` first for any implementation task.

## Task Routing

| Task / Intent | Primary Skill | Supporting |
|---|---|---|
| Any implementation, refactor, or review | `best-practices` | — |
| Add a Gradle module (library / plugin / feature) | `creating-a-module` | `best-practices` |
| Modify build-logic / convention plugins / flavors | `creating-a-module` | — |
| Build or change a Compose screen | `composing-screens` | `best-practices` |
| Write a Maestro UI flow / add test tags | `writing-maestro-tests` | `composing-screens` |
| Create an API endpoint / network call | `integrating-network` | `best-practices`, `creating-a-module` |
| Add error mapping / new failure type | `integrating-network` | `best-practices` |
| Add an analytics event | `best-practices` | — |

## Skill Catalog

| Skill | Directory | Scope |
|---|---|---|
| [[best-practices]] | `best-practices/` | Layering, MVI, repository + ErrorHandler, mappers, DI, test tags, event constants |
| [[creating-a-module]] | `creating-a-module/` | Module type choice, Gradle wiring, convention plugins, dependency rules, order |
| [[composing-screens]] | `composing-screens/` | Route/Screen split, design system reuse, UiText, test tags, previews |
| [[integrating-network]] | `integrating-network/` | Retrofit API, DTO, mappers, repository via ErrorHandler, DI, ErrorMapper |
| [[writing-maestro-tests]] | `writing-maestro-tests/` | Test-tag convention, testTagsAsResourceId hook, Maestro flow structure, id selectors |

## Module Map

- **`app`** — DI wiring, Navigation3 host, `MainActivity`, flavors, `AppConfig` provider.
- **`feature:<name>`** — UI + ViewModel only (login, market, marketlist, detail, watchlist, profile).
- **`plugin:<name>`** — business domain + data (market, news, user). Owns repositories.
- **`library:<name>`** — pure infra (common, designsystem, error, event, logger, mvi, navigation, network, datastore, websocket).

## Dependency Direction

`feature → plugin → library`. Never `feature → feature` or `library → plugin`.
`app` depends on everything and wires it together.

## Progressive Disclosure

```
_index.md  →  SKILL.md  →  rules/ (deep detail, only if needed)
```
