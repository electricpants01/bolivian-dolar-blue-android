package com.locotoinnovations.bolivianbluedolar.network.binance_search

import retrofit2.http.Body
import retrofit2.http.POST

interface BinanceSearchService {

    @POST("bapi/c2c/v2/friendly/c2c/adv/search")
    suspend fun getBuyPrice(
        @Body body : BinanceSearchRequestBody = BinanceSearchRequestBody(
            asset = "USDT",
            fiat = "BOB",
            tradeType = "BUY",
            page = 1,
            rows = 10,
        )
    ): BinanceSearchResponse

    @POST("bapi/c2c/v2/friendly/c2c/adv/search")
    suspend fun getSellPrice(
        @Body body : BinanceSearchRequestBody = BinanceSearchRequestBody(
            asset = "USDT",
            fiat = "BOB",
            tradeType = "SELL",
            page = 1,
            rows = 10,
        )
    ): BinanceSearchResponse

}