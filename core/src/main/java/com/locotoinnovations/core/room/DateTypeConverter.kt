package com.locotoinnovations.core.room

import androidx.room.TypeConverter
import java.util.Date

/**
 * [TypeConverter] used to work with [Date]. Serialized and de-serialized [Date] should be [Long] representation in UTC format.
 */
object DateTypeConverter {
    @TypeConverter
    fun fromTimestamp(value: Long?): Date? {
        return if (value == null) null else Date(value)
    }

    @TypeConverter
    fun dateToTimestamp(date: Date?): Long? {
        return date?.time
    }
}