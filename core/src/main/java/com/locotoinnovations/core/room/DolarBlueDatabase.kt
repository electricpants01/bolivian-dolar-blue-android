package com.locotoinnovations.core.room

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.locotoinnovations.core.dolarbluedata.dao.DolarBlueDao
import com.locotoinnovations.core.dolarbluedata.dao.DolarBlueEntity
import com.locotoinnovations.core.room.dao.ApiTimestampDao
import com.locotoinnovations.core.room.entity.ApiTimestampEntity

@Database(
    entities = [
        DolarBlueEntity::class,
        ApiTimestampEntity::class
    ],
    autoMigrations = [
    ],
    version = 1,
    exportSchema = true
)
@TypeConverters(value = [DateTypeConverter::class])
abstract class DolarBlueDatabase : RoomDatabase() {

    companion object {
        const val DATABASE_NAME = "dolar_blue_db"
    }

    abstract fun dolarBlueDao(): DolarBlueDao
    abstract fun apiTimestampDao(): ApiTimestampDao
}