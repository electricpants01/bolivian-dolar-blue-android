package com.locotoinnovations.core.room.dao

import androidx.room.Dao
import androidx.room.Query
import com.locotoinnovations.core.room.entity.ApiTimestampEntity
import kotlinx.coroutines.flow.Flow
import java.util.Date

@Dao
abstract class ApiTimestampDao : UtilDao<ApiTimestampEntity> {

    companion object {
        private const val QUERY_SELECT_API_TIMESTAMP_ENTITY = """
            SELECT * FROM ApiTimestamp 
            WHERE api_key = :apiKey
            LIMIT 1
        """
    }

    fun readApiTimestampEntityFlow(apiKey: String): Flow<ApiTimestampEntity?> {
        return readApiTimestampEntityFlowInternal(apiKey = apiKey)
    }

    suspend fun readApiTimestampEntity(apiKey: String): ApiTimestampEntity? {
        return readApiTimestampEntityInternal(apiKey = apiKey)
    }

    @Query(QUERY_SELECT_API_TIMESTAMP_ENTITY)
    protected abstract fun readApiTimestampEntityFlowInternal(
        apiKey: String,
    ): Flow<ApiTimestampEntity?>

    @Query(QUERY_SELECT_API_TIMESTAMP_ENTITY)
    protected abstract suspend fun readApiTimestampEntityInternal(
        apiKey: String,
    ): ApiTimestampEntity?

    @Query(
        """
        SELECT local_id FROM ApiTimestamp 
        WHERE api_key = :apiKey
        LIMIT 1
        """
    )
    protected abstract suspend fun readLocalId(
        apiKey: String,
    ): Long?

    @Query("DELETE FROM ApiTimestamp WHERE last_synced_timestamp < :date")
    abstract suspend fun deleteAllBeforeDate(date: Date): Int?

    @Query(
        """
        DELETE FROM ApiTimestamp 
        WHERE ((api_key == :apiKeyPart) OR (api_key LIKE '%' || :apiKeyPart || '?' || '%'))
        """
    )
    abstract suspend fun deleteAll(
        apiKeyPart: String,
    ): Int

    @Query("SELECT * FROM ApiTimestamp")
    abstract suspend fun readLocalId(): List<ApiTimestampEntity>

    suspend fun upsert(timestampEntity: ApiTimestampEntity): Long {
        return insertOrIgnore(timestampEntity)
    }
}