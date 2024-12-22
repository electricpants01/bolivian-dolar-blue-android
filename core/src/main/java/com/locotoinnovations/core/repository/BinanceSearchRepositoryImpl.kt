package com.locotoinnovations.core.repository

import com.locotoinnovations.core.dolarbluedata.operation.FetchDolarBlueDataOperation
import com.locotoinnovations.core.dolarbluedata.operation.ReadDolarBlueDataOperation
import com.locotoinnovations.core.room.operation.common.DataResult
import javax.inject.Inject

interface BinanceSearchRepository {
    suspend fun fetchDolarBlueData(maxAge: Long) : Pair<Double, Double>
    suspend fun readDolarBlueData()
}

class BinanceSearchRepositoryImpl @Inject constructor(
    private val readDolarBlueDataOperation: ReadDolarBlueDataOperation,
    private val fetchDolarBlueDataOperation: FetchDolarBlueDataOperation,
) : BinanceSearchRepository {

    override suspend fun fetchDolarBlueData(maxAge: Long) = when(val result = fetchDolarBlueDataOperation.fetchBuyDolarBlueData(maxAge)) {
        is DataResult.Failure -> Pair(0.0, 0.0)
        is DataResult.Success -> (result.data as Pair<Double, Double>)
    }

    override suspend fun readDolarBlueData() {
        return readDolarBlueDataOperation.readBuyData()
    }
}