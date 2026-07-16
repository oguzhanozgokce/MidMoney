package app.oguzhanozgokce.midmoney.designsystem.text

import android.content.Context
import androidx.annotation.StringRes
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource

/**
 * A piece of user-facing text produced away from the UI (e.g. a ViewModel) without a [Context].
 * [Resource] carries a string id (+ args) resolved at display time; [Dynamic] wraps already-resolved
 * text such as a server/SDK message. Keeps ViewModels free of Android resources and testable.
 */
sealed interface UiText {

    data class Dynamic(val value: String) : UiText

    class Resource(@StringRes val id: Int, vararg val args: Any) : UiText

    fun asString(context: Context): String = when (this) {
        is Dynamic -> value
        is Resource -> context.getString(id, *args)
    }

    @Composable
    fun asString(): String = when (this) {
        is Dynamic -> value
        is Resource -> stringResource(id, *args)
    }
}
