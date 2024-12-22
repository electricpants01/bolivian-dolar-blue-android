package com.locotoinnovations.core.dolarbluedata.dao

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(
    tableName = DolarBlueEntity.TABLE_NAME,
)
data class DolarBlueEntity(
    @PrimaryKey(autoGenerate = true) @ColumnInfo(name = LOCAL_ID) val localId: Long,
    @ColumnInfo(name = "buy") val buy: Double,
    @ColumnInfo(name = "sell") val sell: Double,
) {
    companion object {
        const val LOCAL_ID = "local_id"
        internal const val TABLE_NAME = "tb_dolar_blue"
    }
}