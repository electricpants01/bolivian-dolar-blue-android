package com.locotoinnovations.core.dolarbluedata.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.locotoinnovations.core.room.dao.UtilDao
import kotlinx.coroutines.flow.Flow

@Dao
abstract class DolarBlueDao : UtilDao<DolarBlueEntity> {

    @Query("DELETE FROM tb_dolar_blue WHERE local_id = :id")
    abstract suspend fun delete(id: Long): Int

    @Query("SELECT * FROM tb_dolar_blue ORDER BY local_id DESC limit 1")
    abstract fun readFlow(): Flow<DolarBlueEntity?>

    @Insert
    abstract suspend fun save(dolarBlueEntity: DolarBlueEntity): Long
}