# :library:designsystem

The MidMoney design system: theming and shared Compose UI. Every UI module theming goes through here
so colors, typography and shape stay consistent.

## Contents

| Type | Purpose |
|---|---|
| `MidMoneyTheme` | App-wide Material 3 theme. Supports light/dark and Android 12+ dynamic color. |
| `Color.kt` / `Type.kt` | Color palette and typography tokens. |

Reusable Composables (buttons, cards, price rows, loading/error states) will be added here as screens
are built, so features never hand-roll their own styled primitives.

## Build setup
Applies `midmoney.android.library` + `midmoney.android.compose`; the Compose dependencies (BOM, UI,
Material 3, tooling) come from the compose convention plugin.
