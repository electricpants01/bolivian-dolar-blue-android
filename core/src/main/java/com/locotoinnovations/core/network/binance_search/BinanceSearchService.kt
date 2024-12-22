package com.locototeam.bolivianbluedolar.network.binance_search

import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface BinanceSearchService {

    @POST("bapi/c2c/v2/friendly/c2c/adv/search")
    fun getBuyPrice(
        @Body body : BinanceSearchRequestBody = BinanceSearchRequestBody(
            asset = "USDT",
            fiat = "BOB",
            tradeType = "BUY",
            page = 1,
            rows = 10,
        )
    ): Call<BinanceSearchResponse>

    @POST("bapi/c2c/v2/friendly/c2c/adv/search")
    fun getSellPrice(
        @Body body : BinanceSearchRequestBody = BinanceSearchRequestBody(
            asset = "USDT",
            fiat = "BOB",
            tradeType = "SELL",
            page = 1,
            rows = 10,
        )
    ): Call<BinanceSearchResponse>

}