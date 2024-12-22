package com.locotoinnovations.core.dolarbluedata.operation

import com.locotoinnovations.core.dolarbluedata.dao.DolarBlueDao

class SaveDolarBlueDataOperation(
    private val dolarBlueDao: DolarBlueDao,
) {
    suspend fun saveBuyData(buy: Double, sell: Double) {
        dolarBlueDao.save(buy, sell)
    }
}