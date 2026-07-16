package app.oguzhanozgokce.midmoney.presantation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import app.oguzhanozgokce.midmoney.designsystem.theme.MidMoneyTheme
import app.oguzhanozgokce.midmoney.navigation.EntryProviderInstaller
import app.oguzhanozgokce.midmoney.navigation.MidMoneyNavDisplay
import app.oguzhanozgokce.midmoney.navigation.Navigator
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject
    lateinit var navigator: Navigator

    @Inject
    lateinit var entryInstallers: Set<@JvmSuppressWildcards EntryProviderInstaller>

    private val viewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MidMoneyTheme {
                val uiState by viewModel.uiState.collectAsState()
                MidMoneyNavDisplay(
                    startDestination = uiState.startDestination,
                    navigator = navigator,
                    entryInstallers = entryInstallers,
                )
            }
        }
    }
}
