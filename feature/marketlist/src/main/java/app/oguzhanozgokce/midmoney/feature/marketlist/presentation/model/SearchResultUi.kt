package app.oguzhanozgokce.midmoney.feature.marketlist.presentation.model

import app.oguzhanozgokce.midmoney.plugin.market.domain.model.SymbolMatch

data class SearchResultUi(
    val symbol: String,
    val description: String,
)

fun SymbolMatch.toUi(): SearchResultUi = SearchResultUi(
    symbol = symbol,
    description = description,
)
