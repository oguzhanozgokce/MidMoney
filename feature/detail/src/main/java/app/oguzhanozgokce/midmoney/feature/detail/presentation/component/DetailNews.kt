package app.oguzhanozgokce.midmoney.feature.detail.presentation.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import app.oguzhanozgokce.midmoney.designsystem.component.MidMoneyNetworkImage
import app.oguzhanozgokce.midmoney.designsystem.component.MidMoneyShimmer
import app.oguzhanozgokce.midmoney.feature.detail.presentation.model.NewsUi

@Composable
internal fun NewsSection(
    news: List<NewsUi>,
    isLoading: Boolean,
    onOpen: (String) -> Unit,
) {
    if (!isLoading && news.isEmpty()) return
    Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
        Text(
            text = "News",
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onBackground,
        )
        if (isLoading) {
            repeat(3) { NewsCardSkeleton() }
        } else {
            news.forEach { article ->
                NewsCard(article = article, onClick = { onOpen(article.url) })
            }
        }
    }
}

@Composable
private fun NewsCard(article: NewsUi, onClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        MidMoneyNetworkImage(
            url = article.imageUrl,
            contentDescription = null,
            modifier = Modifier
                .size(72.dp)
                .clip(RoundedCornerShape(12.dp)),
        )
        Column(
            modifier = Modifier
                .weight(1f)
                .height(72.dp),
            verticalArrangement = Arrangement.SpaceBetween,
        ) {
            Text(
                text = article.headline,
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Medium,
                color = MaterialTheme.colorScheme.onSurface,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
            )
            Text(
                text = listOf(article.source, article.dateText).filter { it.isNotBlank() }.joinToString(" · "),
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        }
    }
}

@Composable
private fun NewsCardSkeleton() {
    Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
        MidMoneyShimmer(
            modifier = Modifier
                .size(72.dp)
                .clip(RoundedCornerShape(12.dp)),
        )
        Column(
            modifier = Modifier
                .weight(1f)
                .height(72.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            MidMoneyShimmer(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(16.dp)
                    .clip(RoundedCornerShape(4.dp)),
            )
            MidMoneyShimmer(
                modifier = Modifier
                    .fillMaxWidth(0.5f)
                    .height(12.dp)
                    .clip(RoundedCornerShape(4.dp)),
            )
        }
    }
}
