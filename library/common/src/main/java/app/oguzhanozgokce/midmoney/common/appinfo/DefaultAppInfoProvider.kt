package app.oguzhanozgokce.midmoney.common.appinfo

import android.content.Context
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

internal class DefaultAppInfoProvider @Inject constructor(
    @ApplicationContext private val context: Context,
) : AppInfoProvider {
    override val versionName: String = runCatching {
        context.packageManager.getPackageInfo(context.packageName, 0).versionName
    }.getOrNull().orEmpty()
}
