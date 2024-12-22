package com.locotoinnovations.core.room.operation.apitimestamp.model

import java.util.Date

/**
 * Data class that represents the timestamp of when an [apiKey] was successfully hit.
 * The [apiKey] is typically the url of the endpoint that was successfully executed.
 *
 * [localId] Local id of entry assigned by the database
 * [apiKey] Key to distinguish the api call, usually the url belonging to the api call
 * [lastSyncedTimestamp] The timestamp of the last successful call of the API endpoint corresponding to the [apiKey]
 */
data class ApiTimestamp(
    val localId: Long?,
    val apiKey: String,
    val lastSyncedTimestamp: Date
)