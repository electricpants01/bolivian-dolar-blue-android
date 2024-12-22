package com.locotoinnovations.core.room.operation

import com.locotoinnovations.core.network.NetworkProvider
import com.locotoinnovations.core.network.api.response.ApiResponse
import com.locotoinnovations.core.network.executeNetwork
import com.locotoinnovations.core.room.operation.apitimestamp.ApiTimestampRepository
import com.locotoinnovations.core.room.operation.common.DataResult

/**
 * Fetches a single resource or item from the network. Typically this would be used for show endpoints.
 *
 * See [BaseFetchOperation]
 */
abstract class FetchItemOperation<ResponseType>(
    apiTimestampRepository: ApiTimestampRepository,
    networkProvider: NetworkProvider = NetworkProvider(),
) : BaseFetchOperation<ResponseType>(
    networkProvider,
    apiTimestampRepository
) {

    /**
     * Execute API call and persist data to storage.
     *
     * @return [DataResult] which indicates operation status
     */
    final override suspend fun fetchData(): DataResult<*> {
        // Clone in case fetchData called multiple times, executed Call will throw exception
        return handleApiResponse(apiCall.clone().executeNetwork())
    }

    protected abstract suspend fun handleApiResponse(apiResponse: ApiResponse<ResponseType>): DataResult<*>
}