package com.locotoinnovations.core.repository

import com.locotoinnovations.core.dolarbluedata.operation.FetchDolarBlueDataOperation
import com.locotoinnovations.core.dolarbluedata.operation.ReadDolarBlueDataOperation
import com.locotoinnovations.core.room.operation.common.DataResult
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

interface BinanceSearchRepository {
    suspend fun fetchDolarBlueData(maxAge: Long) : Pair<Double, Double>
    suspend fun readDolarBlueData(): Flow<Pair<Double, Double>>
}

class BinanceSearchRepositoryImpl @Inject constructor(
    private val readDolarBlueDataOperation: ReadDolarBlueDataOperation,
    private val fetchDolarBlueDataOperation: FetchDolarBlueDataOperation,
) : BinanceSearchRepository {

    override suspend fun fetchDolarBlueData(maxAge: Long): Pair<Double, Double> = when(val result = fetchDolarBlueDataOperation.fetchBuyDolarBlueData(maxAge)) {
        is DataResult.Failure -> Pair(-1.0, -1.0)
        is DataResult.Success<*> -> {
            val data = result.data as? Pair<Double, Double>
            data ?: Pair(-1.0, -1.0)
        }
    }

    override suspend fun readDolarBlueData(): Flow<Pair<Double, Double>> {
        return readDolarBlueDataOperation.readBuyData()
    }
}