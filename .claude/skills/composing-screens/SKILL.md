---
name: composing-screens
description: >
  How to build a MidMoney Compose screen: the Route/Screen split, reusing the design
  system, UiText for strings, effect collection, test tags for Maestro, and previews.
  Use when creating or changing any *Screen, *Route, or UI component.
allowed-tools:
  - Read
  - Glob
  - Grep
  - Edit
  - Write
  - Bash
---

# Composing Screens

## Route / Screen split

- `XRoute(viewModel = hiltViewModel())` — collects `uiState`, collects `uiEffect`, calls
  `onAction`. This is the only stateful, Hilt-aware composable.
- `XScreen(uiState, onAction)` — stateless, no ViewModel, no `Context`. Fully preview-able.

```kotlin
@Composable
fun XRoute(viewModel: XViewModel = hiltViewModel()) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val context = LocalContext.current
    LaunchedEffect(Unit) {
        viewModel.uiEffect.collect { effect ->
            when (effect) {
                is XUiEffect.ShowMessage -> context.showToast(effect.message.asString(context))
            }
        }
    }
    XScreen(uiState = uiState, onAction = viewModel::onAction)
}
```

## State, effects, text

- Read everything from `uiState`. Send every user action through `onAction`.
- One-shot messages come as a `UiEffect` and are shown with a toast (`context.showToast`).
- Resolve text with `stringResource` or `uiText.asString()`. No hardcoded user strings.
- Do no formatting or business logic here — the ViewModel/mapper already did it.

## Design system

- Reuse `MidMoney*` components: `MidMoneyScaffold`, `MidMoneyButton`, `MidMoneyTextField`,
  `MidMoneyQuoteRow`, `MidMoneyEmptyState`, `MidMoneyScreenHeader`, `MidMoneyNetworkImage`, etc.
- Do not hand-roll something the design system already provides.
- Colors from `MaterialTheme.colorScheme` / `MidMoneyTheme.extraColors` (price up/down). No `Color(0x…)`.

## Test tags (Maestro contract)

- Tag every field, button, and asserted element. Keys in a per-feature object, never inline.

```kotlin
object LoginTestTags { const val EMAIL = "login.email"; const val SUBMIT = "login.submit" }

MidMoneyTextField(
    value = uiState.email,
    onValueChange = { onAction(LoginUiAction.EmailChanged(it)) },
    modifier = Modifier.fillMaxWidth().testTag(LoginTestTags.EMAIL),
)
```

- Dot-namespace by feature (`login.email`). Maestro selects with `id: login.email`.
- If a design-system component swallows `Modifier`, ensure it forwards it to its root.

## Previews

- Provide `@PreviewLightDark` previews of `XScreen` using a `PreviewParameterProvider`.
- Cover the real states: loading, content, empty, and error.
- Previews call `XScreen` (stateless) inside `MidMoneyTheme { }`, never `XRoute`.

## Keyboard & insets

- Apply `imePadding()` on scrollable forms so fields stay above the keyboard.
- Prefer a toast over a Snackbar for transient messages — it is not clipped by the IME.

## Red flags

- A ViewModel or `Context` used inside `XScreen`.
- Formatting, sorting, or mapping done in a `@Composable`.
- Inline test tags or hardcoded colors/strings.
- A Snackbar hidden behind the keyboard.
