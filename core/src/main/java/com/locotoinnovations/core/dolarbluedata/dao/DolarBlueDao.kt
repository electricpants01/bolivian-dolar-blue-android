package com.locotoinnovations.core.dolarbluedata.dao

import androidx.room.Dao
import androidx.room.Query
import com.locotoinnovations.core.room.dao.UtilDao

@Dao
abstract class DolarBlueDao : UtilDao<DolarBlueEntity>() {

    @Query("DELETE FROM tb_dolar_blue WHERE id = :id")
    abstract suspend fun delete(id: Long): Int

    @Query("SELECT * FROM tb_dolar_blue ORDER BY id DESC limit 1")
    abstract suspend fun read(): DolarBlueEntity?

    @Query("INSERT INTO tb_dolar_blue (buy, sell) VALUES (:buy, :sell)")
    abstract suspend fun save(buy: Double, sell: Double): Long
}