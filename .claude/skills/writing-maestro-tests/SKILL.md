---
name: writing-maestro-tests
description: >
  How to write Maestro UI flows for MidMoney: the test-tag convention on composables, the
  testTagsAsResourceId hook, flow file structure with id selectors, and running the tests.
  Use when adding a Maestro flow or adding test tags to a screen.
allowed-tools:
  - Read
  - Glob
  - Grep
  - Edit
  - Write
  - Bash
---

# Writing Maestro Tests

Flows live in `.maestro/`. They select composables by `id:`, which maps to a Compose `testTag`.

## Prerequisite: the resource-id hook

Maestro only sees a Compose `testTag` as `id:` when the app root enables it (already done in
`MainActivity`):

```kotlin
Box(modifier = Modifier.fillMaxSize().semantics { testTagsAsResourceId = true }) { /* content */ }
```

Do not remove this. Without it, `id:` selectors match nothing.

## Test-tag convention

- One object per screen, in that feature's `presentation` package: `object XTestTags`.
- Keys are `const val`, dot-namespaced `feature.element`, never inline string literals.
- Tag every element the test **interacts with or asserts**: the screen root, inputs, buttons,
  and any element used as a visible/not-visible signal. Skip purely decorative elements.
- Apply with `Modifier.testTag(...)`. Keep it first in the modifier chain for clarity.

```kotlin
object LoginTestTags {
    const val SCREEN = "login.screen"
    const val EMAIL = "login.email"
    const val PASSWORD = "login.password"
    const val SUBMIT = "login.submit"
}

MidMoneyTextField(
    value = uiState.email,
    onValueChange = { onAction(LoginUiAction.EmailChanged(it)) },
    modifier = Modifier.fillMaxWidth().testTag(LoginTestTags.EMAIL),
)
```

- If a design-system component ignores `Modifier`, fix it to forward `modifier` to its root.
- Tags are a test contract. Renaming one breaks flows — change both together.

## Flow structure

```yaml
appId: app.oguzhanozgokce.midmoney
name: Login - sign in with demo account
---
- launchApp:
    clearState: true
- assertVisible:
    id: "login.screen"
- tapOn:
    id: "login.email"
- inputText: "oguzhanozgokce@gmail.com"
- tapOn:
    id: "login.password"
- inputText: "1234567"
- hideKeyboard
- tapOn:
    id: "login.submit"
- extendedWaitUntil:
    notVisible:
      id: "login.screen"
    timeout: 20000
```

## Rules

- Select by `id:`, not `text:`. Ids are locale-independent; text changes with EN/TR.
- `launchApp: { clearState: true }` for a clean start (fresh login).
- Use `extendedWaitUntil` with a timeout for async work (auth, network) instead of a fixed sleep.
- `hideKeyboard` before tapping a control the keyboard may cover.
- Assert success by a state change (login screen no longer visible), not by a fragile text match.
- One flow per user journey. Keep it short and readable.

## Running

```bash
./gradlew :app:installProdDebug
maestro test .maestro/login.yaml
```

Needs a running emulator or device. The login uses "sign in or auto-register", so the demo
account is created on first run and reused afterwards.

## Red flags

- `id:` selectors with no matching `testTag`, or the resource-id hook removed.
- Inline tag literals instead of a per-screen `*TestTags` object.
- Asserting localized `text:` instead of a stable `id:`.
- Fixed `wait` sleeps instead of `extendedWaitUntil`.
