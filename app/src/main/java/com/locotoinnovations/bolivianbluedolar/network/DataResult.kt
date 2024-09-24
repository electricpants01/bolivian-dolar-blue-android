package com.locotoinnovations.bolivianbluedolar.network

/**
 * Create data result class to handle the response from the network
 */
sealed class DataResult<out T> {
    data class Success<out T>(val data: T) : DataResult<T>()

    sealed class Failure : DataResult<Nothing>() {
        data class NetworkError(val message: String, val cause: Throwable? = null) : Failure()
        data class NotFound(val message: String) : Failure()
        data class UnknownError(val message: String, val cause: Throwable? = null) : Failure()
    }
}