package com.locotoinnovations.core.dolarbluedata.operation

import com.locotoinnovations.core.dolarbluedata.dao.DolarBlueDao

class DeleteDolarBlueDataOperation(
    private val dolarBlueDao: DolarBlueDao,
) {
    suspend fun deleteBlueData(id: Long) {
        dolarBlueDao.delete(id)
    }
}