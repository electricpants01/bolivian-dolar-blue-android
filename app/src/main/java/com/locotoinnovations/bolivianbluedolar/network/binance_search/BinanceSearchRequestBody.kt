package com.locotoinnovations.bolivianbluedolar.network.binance_search

data class BinanceSearchRequestBody(
    val asset: String,
    val fiat: String,
    val tradeType: String,
    val page: Int,
    val rows: Int
)