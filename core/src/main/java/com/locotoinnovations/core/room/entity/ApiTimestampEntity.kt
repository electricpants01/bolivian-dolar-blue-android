package com.locotoinnovations.core.room.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date

/**
 * Entity that represents the last time a url has been successfully hit in order
 * to calculate the max age of a dataset.
 *
 * Rather, these values can be empty strings. This is done to make unique index work,
 * since it requires all parts to be non-null.
 */
@Entity(
    tableName = ApiTimestampEntity.TABLE_NAME,
)
data class ApiTimestampEntity(
    @PrimaryKey(autoGenerate = true) @ColumnInfo(name = Column.LOCAL_ID) val localId: Long?,
    @ColumnInfo(name = Column.API_KEY) val apiKey: String,
    @ColumnInfo(name = Column.LAST_SYNCED_TIMESTAMP) val lastSyncedTimestamp: Date,
) {
    companion object {
        internal const val TABLE_NAME = "ApiTimestamp"
    }

    internal object Column {
        const val LOCAL_ID = "local_id"
        const val API_KEY = "api_key"
        const val LAST_SYNCED_TIMESTAMP = "last_synced_timestamp"
    }
}