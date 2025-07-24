package com.locototeam.bolivianbluedolar.ui.screen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.locotoinnovations.core.arch.DolarBlueScaffold
import com.locotoinnovations.core.arch.dolarBlueScaffold
import com.locotoinnovations.core.repository.BinanceSearchRepository
import com.locotoinnovations.core.repository.DEFAULT_MAX_AGE
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainScreenViewModel @Inject constructor(
    private val binanceSearchRepository: BinanceSearchRepository,
): ViewModel(), DolarBlueScaffold<MainScreenState, MainScreenEvent, MainScreenSideEffect> by dolarBlueScaffold(
    initialState = MainScreenState()
){

    override fun handleEvent(event: MainScreenEvent) {
        when(event) {
            is MainScreenEvent.initContent -> initContent()
            is MainScreenEvent.refreshData -> refreshData()
        }
    }

    private fun initContent() {
        viewModelScope.launch {
            binanceSearchRepository.fetchDolarBlueData(DEFAULT_MAX_AGE)

            binanceSearchRepository.readDolarBlueData().collect { (buyPrice, sellPrice) ->
                updateViewState {
                    it.copy(
                        isLoading = false,
                        buyPrice = buyPrice,
                        sellPrice = sellPrice

                    )
                }
            }
        }
    }

    fun refreshData() {
        viewModelScope.launch {
            binanceSearchRepository.fetchDolarBlueData(0)
        }
    }
}

internal data class MainScreenState(
    val isLoading: Boolean = true,
    val buyPrice: Double? = null,
    val sellPrice: Double? = null,
)

interface MainScreenEvent {
    data object initContent: MainScreenEvent
    data object refreshData: MainScreenEvent
}

internal interface MainScreenSideEffect {

}