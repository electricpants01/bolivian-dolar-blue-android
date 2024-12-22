package com.locotoinnovations.core.dolarbluedata.operation

import com.locotoinnovations.core.dolarbluedata.dao.DolarBlueDao
import com.locotoinnovations.core.dolarbluedata.dao.DolarBlueEntity

class SaveDolarBlueDataOperation(
    private val dolarBlueDao: DolarBlueDao,
) {
    suspend fun saveBuyData(buy: Double, sell: Double) {
        dolarBlueDao.save(DolarBlueEntity(0, buy, sell))
    }
}