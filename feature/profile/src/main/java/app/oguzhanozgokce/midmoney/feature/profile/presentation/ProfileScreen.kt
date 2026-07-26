package app.oguzhanozgokce.midmoney.feature.profile.presentation

import androidx.annotation.StringRes
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.HelpOutline
import androidx.compose.material.icons.automirrored.outlined.Logout
import androidx.compose.material.icons.automirrored.outlined.ReceiptLong
import androidx.compose.material.icons.outlined.DarkMode
import androidx.compose.material.icons.outlined.Language
import androidx.compose.material.icons.outlined.Lock
import androidx.compose.material.icons.outlined.Notifications
import androidx.compose.material.icons.outlined.QuestionAnswer
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import app.oguzhanozgokce.midmoney.common.extensions.showToast
import app.oguzhanozgokce.midmoney.designsystem.component.MidMoneyMenuRow
import app.oguzhanozgokce.midmoney.designsystem.component.MidMoneyScaffold
import app.oguzhanozgokce.midmoney.designsystem.component.MidMoneyScreenHeader
import app.oguzhanozgokce.midmoney.designsystem.theme.MidMoneyTheme
import app.oguzhanozgokce.midmoney.feature.profile.R

@Composable
fun ProfileRoute(viewModel: ProfileViewModel = hiltViewModel()) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val context = LocalContext.current

    LaunchedEffect(Unit) {
        viewModel.uiEffect.collect { effect ->
            when (effect) {
                is ProfileUiEffect.ShowMessage -> context.showToast(effect.message.asString(context))
            }
        }
    }

    ProfileScreen(uiState = uiState, onAction = viewModel::onAction)
}

@Composable
private fun ProfileScreen(
    uiState: ProfileUiState,
    onAction: (ProfileUiAction) -> Unit,
) {
    val groups = remember { profileMenuGroups() }

    MidMoneyScaffold { padding ->
        Column(
            modifier = Modifier
                .testTag(ProfileTestTags.SCREEN)
                .fillMaxSize()
                .padding(padding)
                .verticalScroll(rememberScrollState()),
        ) {
            MidMoneyScreenHeader(title = stringResource(R.string.profile_title))
            uiState.environmentLabel?.let { label ->
                Text(
                    text = label,
                    style = MaterialTheme.typography.labelSmall,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onPrimary,
                    modifier = Modifier
                        .testTag(ProfileTestTags.ENV_BADGE)
                        .padding(start = 16.dp)
                        .clip(RoundedCornerShape(6.dp))
                        .background(MaterialTheme.colorScheme.primary)
                        .padding(horizontal = 8.dp, vertical = 2.dp),
                )
            }
            UserHeader(email = uiState.email)

            groups.forEachIndexed { groupIndex, group ->
                Spacer(modifier = Modifier.size(16.dp))
                val isLastGroup = groupIndex == groups.lastIndex
                group.forEachIndexed { index, item ->
                    val showDivider = index < group.lastIndex || isLastGroup
                    MidMoneyMenuRow(
                        icon = item.icon,
                        title = stringResource(item.titleRes),
                        onClick = { onAction(ProfileUiAction.ComingSoonClicked(item.key)) },
                        showDivider = showDivider,
                        modifier = Modifier.testTag(ProfileTestTags.menu(item.key)),
                    )
                }
            }

            MidMoneyMenuRow(
                icon = Icons.AutoMirrored.Outlined.Logout,
                title = stringResource(R.string.profile_logout),
                onClick = { onAction(ProfileUiAction.Logout) },
                tint = MaterialTheme.colorScheme.error,
                showChevron = false,
                showDivider = false,
                modifier = Modifier.testTag(ProfileTestTags.LOGOUT),
            )
            Text(
                text = stringResource(R.string.profile_version, uiState.versionName),
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .testTag(ProfileTestTags.VERSION)
                    .fillMaxWidth()
                    .padding(vertical = 16.dp),
            )
        }
    }
}

private data class MenuItem(
    val icon: ImageVector,
    @StringRes val titleRes: Int,
    val key: String,
)

private fun profileMenuGroups(): List<List<MenuItem>> = listOf(
    listOf(
        MenuItem(Icons.AutoMirrored.Outlined.ReceiptLong, R.string.profile_transactions, "transactions"),
        MenuItem(Icons.Outlined.Notifications, R.string.profile_notifications, "notifications"),
    ),
    listOf(
        MenuItem(Icons.Outlined.DarkMode, R.string.profile_appearance, "appearance"),
        MenuItem(Icons.Outlined.Language, R.string.profile_language, "language"),
        MenuItem(Icons.Outlined.Lock, R.string.profile_security, "security"),
    ),
    listOf(
        MenuItem(Icons.AutoMirrored.Outlined.HelpOutline, R.string.profile_help, "help"),
        MenuItem(Icons.Outlined.QuestionAnswer, R.string.profile_faq, "faq"),
    ),
)

@Composable
private fun UserHeader(email: String?) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        Avatar(email = email)
        Column {
            Text(
                text = email ?: stringResource(R.string.profile_signed_in),
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onBackground,
            )
            Text(
                text = stringResource(R.string.profile_account),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        }
    }
}

@Composable
private fun Avatar(email: String?) {
    Box(
        modifier = Modifier
            .size(56.dp)
            .clip(CircleShape)
            .background(MaterialTheme.colorScheme.primary),
        contentAlignment = Alignment.Center,
    ) {
        Text(
            text = email?.take(1)?.uppercase() ?: "?",
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onPrimary,
        )
    }
}

@PreviewLightDark
@Composable
private fun ProfileScreenPreview(
    @PreviewParameter(ProfileUiStatePreviewProvider::class) state: ProfileUiState,
) {
    MidMoneyTheme {
        ProfileScreen(uiState = state, onAction = {})
    }
}
