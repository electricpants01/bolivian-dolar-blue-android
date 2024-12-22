package com.locotoinnovations.core.network.api.response

import com.locotoinnovations.core.room.operation.common.DataResult

/**
 * Represents the result from an operation that was executed in the network layer.
 * For the failure case, the network operation could have failed for any number
 * of reasons so [NetworkError] describes the exact error that happened.
 */
sealed class NetworkResult<out ResultType> {
    data class Success<ResultType>(val data: ResultType) : NetworkResult<ResultType>()
    data class Failure(val error: NetworkError) : NetworkResult<Nothing>()

    /**
     * Method to map a [NetworkResult] to a [DataResult] object
     */
    fun toDataResult(): DataResult<ResultType> {
        return when (this) {
            is Success -> DataResult.Success(data)
            is Failure -> DataResult.Failure(error.toDataError())
        }
    }
}

fun ApiResponse.Error.toNetworkFailure() = NetworkResult.Failure(toNetworkError())
fun ApiResponse.Empty.toNetworkFailure() = NetworkResult.Failure(toNetworkError())
fun ApiResponse.Empty.toNetworkSuccess() = NetworkResult.Success(null)
fun <T> ApiResponse.Success<T>.toNetworkSuccess() = NetworkResult.Success(body)