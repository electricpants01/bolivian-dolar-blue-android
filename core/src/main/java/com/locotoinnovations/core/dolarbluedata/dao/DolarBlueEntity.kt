package com.locotoinnovations.core.dolarbluedata.dao

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(
    tableName = DolarBlueEntity.TABLE_NAME,
)
data class DolarBlueEntity(
    @PrimaryKey(autoGenerate = true) @ColumnInfo(name = "id") val id: Long,
    @ColumnInfo(name = "buy") val buy: Double,
    @ColumnInfo(name = "sell") val sell: Double,
    val dolarBlue: Double,
) {
    companion object {
        internal const val TABLE_NAME = "tb_dolar_blue"
    }
}