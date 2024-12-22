package com.locotoinnovations.core.dolarbluedata.operation

import com.locotoinnovations.core.dolarbluedata.dao.DolarBlueDao
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class ReadDolarBlueDataOperation(
    private val dolarBlueDao: DolarBlueDao,
) {

    suspend fun readBuyData(): Flow<Pair<Double, Double>> =
        dolarBlueDao.readFlow().map { Pair(it?.buy ?: 0.0, it?.sell ?: 0.0) }
}