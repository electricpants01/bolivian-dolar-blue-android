package com.locotoinnovations.bolivianbluedolar.network.binance_search

data class BinanceSearchResponse(
    val code: String,
    val `data`: List<Data>,
    val message: Any,
    val messageDetail: Any
) {
    data class Data(
        val adv: Adv
    ) {
        data class Adv(
            val advNo: String,
            val advStatus: Any,
            val asset: String,
            val classify: String,
            val currencyRate: Any,
            val fiatUnit: String,
            val price: String,
            val priceFloatingRatio: Any,
            val priceType: Any,
            val rateFloatingRatio: Any,
            val tradeType: String
        )
    }
}