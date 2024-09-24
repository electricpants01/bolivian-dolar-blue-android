package com.locotoinnovations.bolivianbluedolar.ui.screen

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.locotoinnovations.bolivianbluedolar.network.DataResult
import com.locotoinnovations.bolivianbluedolar.ui.components.CurrencyConverterCard
import com.locotoinnovations.bolivianbluedolar.ui.components.ExchangeRateCard
import com.locotoinnovations.bolivianbluedolar.ui.components.RefreshShareCard
import kotlinx.coroutines.launch

@Composable
fun MainScreen(
    modifier: Modifier,
    shouldShowNotificationButton: Boolean,
    mainScreenViewModel: MainScreenViewModel = hiltViewModel(),
    onRequestNotificationPermission: () -> Unit
) {

    val uiState by mainScreenViewModel.uiState.collectAsStateWithLifecycle(initialValue = MainScreenState())
    // MutableState for buy and sell prices
    val buyPriceState = remember { mutableStateOf<Double?>(null) }
    val sellPriceState = remember { mutableStateOf<Double?>(null) }

    val snackbarHostState = remember { SnackbarHostState() }
    val coroutineScope = rememberCoroutineScope()

    // LaunchedEffect to set values when uiState changes
    LaunchedEffect(uiState) {
        buyPriceState.value = when (val result = uiState.buyPrice) {
            is DataResult.Success -> result.data
            else -> null // Handle other cases as needed
        }

        sellPriceState.value = when (val result = uiState.sellPrice) {
            is DataResult.Success -> result.data
            else -> null // Handle other cases as needed
        }
    }

    Column(
        modifier = modifier,
    ) {
        if (uiState.isLoading) {
            Text(text = "Loading...")
        } else {
            if (buyPriceState.value != null && sellPriceState.value != null) {
                Column {
                    Box(modifier = Modifier.weight(0.3f))

                    ExchangeRateCard(
                        buyPrice = String.format(locale = null, "%.2f", buyPriceState.value),
                        sellPrice = String.format(locale = null, "%.2f", sellPriceState.value),
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    CurrencyConverterCard(sellPrice = sellPriceState.value!!)

                    Spacer(modifier = Modifier.height(16.dp))

                    RefreshShareCard(
                        shouldShowNotificationPermissionButton = shouldShowNotificationButton,
                        onUpdateClick = {
                            coroutineScope.launch {
                                mainScreenViewModel.fetchBuyPrice()
                                mainScreenViewModel.fetchSellPrice()
                                snackbarHostState.showSnackbar("Datos Actualizados")
                            }
                        },
                        onShareClick = {
                            // TODO: Share the buy and sell prices
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
}