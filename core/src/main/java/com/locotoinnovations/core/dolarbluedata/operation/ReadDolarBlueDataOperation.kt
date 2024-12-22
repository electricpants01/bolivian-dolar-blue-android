package com.locotoinnovations.core.dolarbluedata.operation

import com.locotoinnovations.core.dolarbluedata.dao.DolarBlueDao

class ReadDolarBlueDataOperation(
    private val dolarBlueDao: DolarBlueDao,
) {

    suspend fun readBuyData() {
        dolarBlueDao.read()
    }
}