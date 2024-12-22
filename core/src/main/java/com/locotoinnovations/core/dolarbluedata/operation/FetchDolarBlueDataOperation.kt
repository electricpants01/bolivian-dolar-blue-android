package com.locotoinnovations.core.dolarbluedata.operation

import com.locotoinnovations.core.network.NetworkProvider
import com.locotoinnovations.core.network.api.response.ApiResponse
import com.locotoinnovations.core.room.operation.FetchItemOperation
import com.locotoinnovations.core.room.operation.apitimestamp.ApiTimestampRepository
import com.locotoinnovations.core.room.operation.common.DataResult
import com.locototeam.bolivianbluedolar.network.binance_search.BinanceSearchResponse
import com.locototeam.bolivianbluedolar.network.binance_search.BinanceSearchService
import retrofit2.Call
import retrofit2.await

class FetchDolarBlueDataOperation(
    private val binanceSearchService: BinanceSearchService,
    private val apiTimestampRepository: ApiTimestampRepository,
    private val networkProvider: NetworkProvider,
    private val saveDolarBlueDataOperation: SaveDolarBlueDataOperation,
) {
    suspend fun fetchBuyDolarBlueData(maxAge: Long): DataResult<*> {
        return object : FetchItemOperation<BinanceSearchResponse>(
            apiTimestampRepository = apiTimestampRepository,
            networkProvider = networkProvider,
        ) {
            override val apiCall: Call<BinanceSearchResponse>
                get() = binanceSearchService.getBuyPrice()

            override suspend fun hasPermissions(): Boolean = true

            override suspend fun handleApiResponse(apiResponse: ApiResponse<BinanceSearchResponse>): DataResult<Pair<Double,Double>> {
                val buy: Double = binanceSearchService.getBuyPrice().await().let {
                    val prices = it.data.map { it.adv.price }
                    val average = prices.sumOf { it.toDouble() } / prices.size
                    average
                }
                val sell = binanceSearchService.getSellPrice().await().let {
                    val prices = it.data.map { it.adv.price }
                    val average = prices.sumOf { it.toDouble() } / prices.size
                    average
                }
                saveDolarBlueDataOperation.saveBuyData(buy, sell)

                return DataResult.Success(data = Pair(buy, sell))
            }
        }.execute(maxAge)
    }
}