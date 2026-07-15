package app.oguzhanozgokce.midmoney

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import app.oguzhanozgokce.midmoney.designsystem.theme.MidMoneyTheme
import app.oguzhanozgokce.midmoney.navigation.EntryProviderInstaller
import app.oguzhanozgokce.midmoney.navigation.Navigator
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject
    lateinit var navigator: Navigator

    @Inject
    lateinit var entryInstallers: Set<@JvmSuppressWildcards EntryProviderInstaller>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MidMoneyTheme {
                MidMoneyNavDisplay(
                    navigator = navigator,
                    entryInstallers = entryInstallers,
                )
            }
        }
    }
}
