package com.locotoinnovations.bolivianbluedolar.ui.screen

import android.content.Intent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.locotoinnovations.bolivianbluedolar.ui.components.CurrencyConverterCard
import com.locotoinnovations.bolivianbluedolar.ui.components.ExchangeRateCard
import com.locotoinnovations.bolivianbluedolar.ui.components.RefreshShareCard
import kotlinx.coroutines.launch

@Composable
fun MainScreen(
    modifier: Modifier,
    snackbarHostState: SnackbarHostState,
    shouldShowNotificationButton: Boolean,
    mainScreenViewModel: MainScreenViewModel = hiltViewModel(),
    onRequestNotificationPermission: () -> Unit
) {

    val uiState by mainScreenViewModel.uiState.collectAsStateWithLifecycle(initialValue = MainScreenState())
    val coroutineScope = rememberCoroutineScope()

    // Obtener el contexto dentro de la función composable
    val context = LocalContext.current

    Column(
        modifier = modifier,
    ) {
        if (uiState.isLoading) {
            Text(text = "Loading...")
        } else {
            Column {
                Box(modifier = Modifier.weight(0.3f))

                ExchangeRateCard(
                    buyPrice = String.format(locale = null, "%.2f", uiState.buyPrice ?: 0.0),
                    sellPrice = String.format(locale = null, "%.2f", uiState.sellPrice ?: 0.0),
                )

                Spacer(modifier = Modifier.height(16.dp))

                CurrencyConverterCard(sellPrice = uiState.sellPrice ?: 0.0)

                Spacer(modifier = Modifier.height(16.dp))

                RefreshShareCard(
                    shouldShowNotificationPermissionButton = shouldShowNotificationButton,
                    onUpdateTapped = {
                        coroutineScope.launch {
                            mainScreenViewModel.fetchBuyPrice()
                            mainScreenViewModel.fetchSellPrice()
                            snackbarHostState.showSnackbar("Datos Actualizados")
                        }
                    },
                    onShareTapped = {
                        // Share the buy and sell prices
                        val shareText = "Buy Price: ${uiState.buyPrice ?: 0.0}, Sell Price: ${uiState.sellPrice ?: 0.0}"
                        val sendIntent = Intent().apply {
                            action = Intent.ACTION_SEND
                            putExtra(Intent.EXTRA_TEXT, shareText)
                            type = "text/plain"
                        }
                        val shareIntent = Intent.createChooser(sendIntent, null)

                        // Usa el contexto para iniciar la actividad
                        context.startActivity(shareIntent)

                        coroutineScope.launch {
                            snackbarHostState.showSnackbar("Compartir")
                        }
                    },
                    onReceiveNotificationClick = {
                        onRequestNotificationPermission()
                    }
                )

                Box(modifier = Modifier.weight(1f))
            }
        }
    }
}