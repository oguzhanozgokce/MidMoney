package app.oguzhanozgokce.midmoney.designsystem.component

/**
 * A single filter chip: its [label] and an optional [testTag] kept together so callers pass one
 * list of models instead of index-aligned parallel lists.
 */
data class MidMoneyFilterChip(
    val label: String,
    val testTag: String? = null,
)
