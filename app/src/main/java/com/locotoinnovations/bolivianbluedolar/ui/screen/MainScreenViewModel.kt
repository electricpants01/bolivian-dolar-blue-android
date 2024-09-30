package com.locotoinnovations.bolivianbluedolar.ui.screen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.locotoinnovations.bolivianbluedolar.network.DataResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@HiltViewModel
class MainScreenViewModel @Inject constructor(
    private val binanceSearchRepository: BinanceSearchRepository,
): ViewModel() {

    private val _uiState = MutableStateFlow(MainScreenState())
    val uiState: Flow<MainScreenState> = _uiState

    init {
        fetchBuyPrice()
        fetchSellPrice()
    }

    fun fetchBuyPrice() {
        binanceSearchRepository.getBuyPrice()
            .onEach { buyPrice ->
                when (buyPrice) {
                    is DataResult.Success -> {
                        _uiState.value = _uiState.value.copy(
                            isLoading = false,
                            buyPrice = buyPrice.data
                        )
                    }
                    is DataResult.Failure -> {
                        _uiState.value = _uiState.value.copy(
                            isLoading = false,
                            buyPrice = null
                        )
                    }
                }
            }.launchIn(viewModelScope)
    }

    fun fetchSellPrice() {
        binanceSearchRepository.getSellPrice()
            .onEach { sellPrice ->
                when (sellPrice) {
                    is DataResult.Success -> {
                        _uiState.value = _uiState.value.copy(
                            isLoading = false,
                            sellPrice = sellPrice.data
                        )
                    }
                    is DataResult.Failure -> {
                        _uiState.value = _uiState.value.copy(
                            isLoading = false,
                            sellPrice = null
                        )
                    }
                }
            }.launchIn(viewModelScope)
    }
}

data class MainScreenState(
    val isLoading: Boolean = true,
    val buyPrice: Double? = null,
    val sellPrice: Double? = null,
)