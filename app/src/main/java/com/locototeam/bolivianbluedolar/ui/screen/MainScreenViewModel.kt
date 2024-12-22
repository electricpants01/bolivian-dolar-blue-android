package com.locototeam.bolivianbluedolar.ui.screen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.locotoinnovations.core.repository.BinanceSearchRepository
import com.locotoinnovations.core.repository.DEFAULT_MAX_AGE
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainScreenViewModel @Inject constructor(
    private val binanceSearchRepository: BinanceSearchRepository,
): ViewModel() {

    private val _uiState = MutableStateFlow(MainScreenState())
    val uiState: Flow<MainScreenState> = _uiState

    init {
        viewModelScope.launch {
            binanceSearchRepository.fetchDolarBlueData(DEFAULT_MAX_AGE)

            binanceSearchRepository.readDolarBlueData().collect { (buyPrice, sellPrice) ->
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    buyPrice = buyPrice,
                    sellPrice = sellPrice
                )
            }
        }
    }

    fun refreshData() {
        viewModelScope.launch {
            binanceSearchRepository.fetchDolarBlueData(0)
        }
    }
}

data class MainScreenState(
    val isLoading: Boolean = true,
    val buyPrice: Double? = null,
    val sellPrice: Double? = null,
)