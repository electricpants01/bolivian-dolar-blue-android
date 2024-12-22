package com.locotoinnovations.core.repository

import com.locotoinnovations.core.room.dao.ApiTimestampDao
import com.locotoinnovations.core.room.entity.ApiTimestampEntity
import com.locotoinnovations.core.room.operation.apitimestamp.ApiTimestampRepository
import com.locotoinnovations.core.room.operation.apitimestamp.model.ApiTimestamp
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

/**
 * Repository class to access [ApiTimestamp]
 */
internal class ApiTimestampRepositoryImpl @Inject constructor(
    private val apiTimestampDao: ApiTimestampDao,
) : ApiTimestampRepository {

    /**
     * Get [ApiTimestamp] for specific [apiKey] if exists
     */
    override fun readEntryFlow(apiKey: String): Flow<ApiTimestamp?> {
        return apiTimestampDao.readApiTimestampEntityFlow(apiKey).map {
            it?.let {
                ApiTimestamp(
                    localId = it.localId,
                    apiKey = it.apiKey,
                    lastSyncedTimestamp = it.lastSyncedTimestamp,
                )
            }
        }
    }

    /**
     * Get [ApiTimestamp] for specific [apiKey] if exists
     */
    override suspend fun readEntry(apiKey: String): ApiTimestamp? {
        return apiTimestampDao.readApiTimestampEntity(apiKey)?.let {
            ApiTimestamp(
                localId = it.localId,
                apiKey = it.apiKey,
                lastSyncedTimestamp = it.lastSyncedTimestamp,
            )
        }
    }

    /**
     * Update/save [entry]
     * If there is [ApiTimestamp] with the same [ApiTimestamp.apiKey] and [scope]
     * it will be replaced, otherwise saved as new entry
     */
    override suspend fun saveEntry(entry: ApiTimestamp) {
        val entity = ApiTimestampEntity(
            localId = null,
            apiKey = entry.apiKey,
            lastSyncedTimestamp = entry.lastSyncedTimestamp,
        )
        apiTimestampDao.upsert(entity)
    }

    override suspend fun deleteEntries(apiKeyPart: String) {
        apiTimestampDao.deleteAll(apiKeyPart = apiKeyPart)
    }
}