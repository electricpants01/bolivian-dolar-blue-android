package com.locotoinnovations.core.room

import androidx.room.Database
import androidx.room.RoomDatabase
import com.locotoinnovations.core.dolarbluedata.dao.DolarBlueDao
import com.locotoinnovations.core.dolarbluedata.dao.DolarBlueEntity

@Database(
    entities = [
        DolarBlueEntity::class
    ],
    autoMigrations = [
    ],
    version = 1,
    exportSchema = true
)
abstract class DolarBlueDatabase : RoomDatabase() {

    companion object {
        const val DATABASE_NAME = "db_dolar_blue"
    }

    abstract fun dolarBlueDao(): DolarBlueDao
}