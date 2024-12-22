package com.locotoinnovations.core.room.operation.apitimestamp

import com.locotoinnovations.core.room.operation.apitimestamp.model.ApiTimestamp
import kotlinx.coroutines.flow.Flow

interface ApiTimestampRepository {
    fun readEntryFlow(apiKey: String): Flow<ApiTimestamp?>
    suspend fun readEntry(apiKey: String): ApiTimestamp?
    suspend fun saveEntry(entry: ApiTimestamp)
    suspend fun deleteEntries(apiKeyPart: String)
}