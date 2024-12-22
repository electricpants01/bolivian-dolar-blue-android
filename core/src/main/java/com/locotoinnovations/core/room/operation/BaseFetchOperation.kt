package com.locotoinnovations.core.room.operation

import com.locotoinnovations.core.network.NetworkProvider
import com.locotoinnovations.core.network.api.response.ApiResponse
import com.locotoinnovations.core.network.api.response.toNetworkFailure
import com.locotoinnovations.core.network.url
import com.locotoinnovations.core.room.operation.apitimestamp.ApiTimestampRepository
import com.locotoinnovations.core.room.operation.apitimestamp.model.ApiTimestamp
import com.locotoinnovations.core.room.operation.common.DataError
import com.locotoinnovations.core.room.operation.common.DataResult
import org.jetbrains.annotations.VisibleForTesting
import retrofit2.Call
import java.util.Date

/**
 * Fetches data from the server. This will perform checks to see if we have internet connection and
 * permissions to hit the network before executing the request. On a successful request, implementors
 * should save the response in storage.
 *
 * When an error occurs, it will translate [NetworkError] or [StorageError] into a [DataError] and
 * return back a [DataResult.Failure]. Otherwise, it will return back a [DataResult.Success]
 */
abstract class BaseFetchOperation<ResponseType>(
    private val networkProvider: NetworkProvider = NetworkProvider(),
    protected val apiTimestampRepository: ApiTimestampRepository,
) {
    /**
     * [Call] needed to hit the server.
     */
    protected abstract val apiCall: Call<ResponseType>

    /**
     * API key used to identify the API call, e.g. to store the proper last sync timestamp.
     */
    protected open val apiKey get() = apiCall.url

    @VisibleForTesting
    internal fun getApiKeyForTesting() = apiKey

    suspend fun execute(maxAge: Long): DataResult<*> {
        // If we don't need to fetch, then no reason to check network or permissions yet.
        if (!shouldFetchFromNetwork(maxAge)) return DataResult.Success(Unit)

        // If we are offline, no need to do a permissions check.
        if (!networkProvider.isConnected()) return DataResult.Failure(DataError.Offline)

        // Lastly check permissions before executing api call.
        if (!hasPermissions()) return DataResult.Failure(DataError.Forbidden)

        val result = fetchData()
        if (result is DataResult.Success<*>) {
            apiTimestampRepository.saveEntry(
                ApiTimestamp(
                    localId = null,
                    lastSyncedTimestamp = Date(),
                    apiKey = apiKey,
                )
            )
        }
        return result
    }

    /**
     * Execute [apiCall] and do data processing
     * This should return [DataResult] which indicates whether the whole operation was successful.
     */
    protected abstract suspend fun fetchData(): DataResult<*>

    /**
     * Implementors should check the permissions required to hit the endpoint to avoid 403s from the network.
     *
     * Reference the permissions matrix to see what permissions are required for that specific API endpoint.
     * If it's not listed there, you may have to ask a backend developer or check the backend code yourself.
     *
     * @see [Procore Permissions Matrix](https://support.procore.com/references/user-permissions-matrix-web)
     */
    protected abstract suspend fun hasPermissions(): Boolean

    /**
     * Method to perform check if it's required to fetch data from API.
     * By default, it uses [apiTimestampRepository] to verify last time when API call was hit.
     * In case data is fresh (last successful fetch not older than [maxAge])
     * this will return false, otherwise true.
     *
     * You can override this method to use own refresh logic.
     */
    protected open suspend fun shouldFetchFromNetwork(maxAge: Long): Boolean {
        if (maxAge <= 0) return true

        val apiTimestampEntry = apiTimestampRepository.readEntry(apiKey) ?: return true
        return System.currentTimeMillis() > apiTimestampEntry.lastSyncedTimestamp.time + maxAge
    }
}

suspend fun <T, R> ApiResponse<T>.toDataResult(
    handleError: suspend (error: ApiResponse.Error) -> DataResult<R> = { it.toNetworkFailure().toDataResult() },
    handleEmpty: suspend (empty: ApiResponse.Empty) -> DataResult<R> = { it.toNetworkFailure().toDataResult() },
    handleSuccess: suspend (response: T) -> DataResult<R>
) : DataResult<R> {
    return when (this) {
        is ApiResponse.Empty -> handleEmpty(this)
        is ApiResponse.Error -> handleError(this)
        is ApiResponse.Success -> handleSuccess(body)
    }
}