package com.locotoinnovations.bolivianbluedolar.ui.screen

import com.locotoinnovations.bolivianbluedolar.network.DataResult
import com.locotoinnovations.bolivianbluedolar.network.binance_search.BinanceSearchService
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject


class BinanceSearchRepository @Inject constructor(
    private val binanceSearchService: BinanceSearchService
) {

    fun getBuyPrice(): Flow<DataResult<Double>> = flow {
        try {
           val buyResponse = binanceSearchService.getBuyPrice()
            val prices = buyResponse.data.map { it.adv.price }
            val average = prices.sumOf { it.toDouble() } / prices.size
            emit(DataResult.Success(average))
        } catch (e: Exception) {
            emit(DataResult.Failure.NetworkError("network Error", e))
        }
    }

    fun getSellPrice(): Flow<DataResult<Double>> = flow {
        try {
           val sellResponse = binanceSearchService.getSellPrice()
            val prices = sellResponse.data.map { it.adv.price }
            val average = prices.sumOf { it.toDouble() } / prices.size
            emit(DataResult.Success(average))
        } catch (e: Exception) {
            emit(DataResult.Failure.NetworkError("network Error", e))
        }
    }
}